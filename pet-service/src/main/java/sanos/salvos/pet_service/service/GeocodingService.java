package sanos.salvos.pet_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
public class GeocodingService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final Map<String, LatLng> cache = new HashMap<>();
    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org";

    public GeocodingService() {
        this.webClient = WebClient.builder()
                .baseUrl(NOMINATIM_URL)
                .defaultHeader("User-Agent", "SanoYSalvos/1.0")
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public LatLng geocode(String address) {
        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("La dirección no puede estar vacía");
        }

        if (cache.containsKey(address)) {
            return cache.get(address);
        }

        try {
            System.out.println("🌐 Geocodificando: " + address);

            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search")
                            .queryParam("q", address)
                            .queryParam("format", "json")
                            .queryParam("limit", 1)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("📨 Respuesta de Nominatim: " + response);

            if (response == null || response.equals("[]") || response.isEmpty()) {
                throw new RuntimeException("No se pudo geocodificar la dirección: " + address);
            }

            JsonNode root = objectMapper.readTree(response);

            if (root == null || !root.isArray() || root.isEmpty()) {
                throw new RuntimeException("No se encontraron resultados para: " + address);
            }

            JsonNode firstResult = root.get(0);

            // Verificar que tiene los campos lat y lon
            if (!firstResult.has("lat") || !firstResult.has("lon")) {
                throw new RuntimeException("La respuesta no contiene coordenadas para: " + address);
            }

            double lat = Double.parseDouble(firstResult.get("lat").asText());
            double lon = Double.parseDouble(firstResult.get("lon").asText());

            System.out.println("✅ Coordenadas obtenidas: " + lat + ", " + lon);

            LatLng result = new LatLng(lat, lon);
            cache.put(address, result);
            return result;

        } catch (Exception e) {
            System.err.println("❌ Error en geocodificación: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al geocodificar la dirección: " + e.getMessage(), e);
        }
    }

    public String reverseGeocode(double lat, double lon) {
        try {
            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/reverse")
                            .queryParam("lat", lat)
                            .queryParam("lon", lon)
                            .queryParam("format", "json")
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (response == null) {
                return "Dirección no disponible";
            }

            JsonNode root = objectMapper.readTree(response);
            JsonNode displayName = root.get("display_name");

            if (displayName == null || displayName.isNull()) {
                return "Dirección no disponible";
            }

            return displayName.asText();

        } catch (Exception e) {
            return "Error al obtener dirección";
        }
    }

    public boolean isAddressSimilar(String address1, String address2) {
        if (address1 == null || address2 == null) return false;
        if (address1.equalsIgnoreCase(address2)) return true;

        String[] words1 = extractKeywords(address1);
        String[] words2 = extractKeywords(address2);

        if (words1.length == 0 || words2.length == 0) return false;

        long matches = Arrays.stream(words1)
                .filter(w1 -> Arrays.stream(words2).anyMatch(w2 -> w2.equalsIgnoreCase(w1)))
                .count();

        return matches >= 2;
    }

    public String[] extractKeywords(String address) {
        if (address == null) return new String[0];

        String clean = address.toLowerCase()
                .replaceAll("[^a-záéíóúñü ]", " ")
                .replaceAll("\\s+", " ")
                .trim();

        return Arrays.stream(clean.split(" "))
                .filter(w -> w.length() > 2)
                .toArray(String[]::new);
    }

    public String getKeywordsAsString(String address) {
        String[] words = extractKeywords(address);
        return String.join(" ", words);
    }

    public static class LatLng {
        private final double lat;
        private final double lon;

        public LatLng(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }

        public double getLat() { return lat; }
        public double getLon() { return lon; }
    }
}