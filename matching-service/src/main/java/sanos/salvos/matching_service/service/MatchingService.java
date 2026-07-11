package sanos.salvos.matching_service.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import sanos.salvos.matching_service.model.PetMatchRequest;
import sanos.salvos.matching_service.model.MatchResponse;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchingService {

    private final WebClient webClient;

    public MatchingService() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8083")
                .build();
    }

    public MatchResponse calculateMatch(PetMatchRequest req) {
        if (req.getPetType() == null || req.getPetType().isEmpty()) {
            throw new IllegalArgumentException("El tipo de mascota es obligatorio");
        }
        if (req.getLatitude() == null || req.getLongitude() == null) {
            throw new IllegalArgumentException("La ubicación es obligatoria");
        }

        String oppositeStatus = req.getReportType().equals("LOST") ? "FOUND" : "LOST";
        List<PetData> candidates = getPetsByStatus(oppositeStatus);

        if (candidates == null || candidates.isEmpty()) {
            return new MatchResponse(0.0, "No hay mascotas registradas para comparar");
        }

        // Filtro 1: Especie exacta
        List<PetData> filteredByType = candidates.stream()
                .filter(p -> p.getType() != null && p.getType().equalsIgnoreCase(req.getPetType()))
                .collect(Collectors.toList());

        if (filteredByType.isEmpty()) {
            return new MatchResponse(0.0, "No hay mascotas de la misma especie");
        }

        // Filtro 2: Raza exacta
        List<PetData> filteredByBreed = filteredByType.stream()
                .filter(p -> p.getBreed() != null && p.getBreed().equalsIgnoreCase(req.getPetBreed()))
                .collect(Collectors.toList());

        if (filteredByBreed.isEmpty()) {
            return new MatchResponse(0.0, "No hay mascotas de la misma raza");
        }

        // Filtro 3: Distancia máxima 50 km
        List<PetData> filteredByLocation = filteredByBreed.stream()
                .filter(p -> {
                    double distance = calculateDistance(
                        req.getLatitude(), req.getLongitude(),
                        p.getLatitude(), p.getLongitude()
                    );
                    return distance <= 50;
                })
                .collect(Collectors.toList());

        if (filteredByLocation.isEmpty()) {
            return new MatchResponse(0.0, "No hay mascotas con ubicación cercana (más de 50 km)");
        }

        // Calcular mejor coincidencia
        double bestScore = 0.0;
        PetData bestMatch = null;

        for (PetData candidate : filteredByLocation) {
            double score = calculateScore(req, candidate);
            if (score > bestScore) {
                bestScore = score;
                bestMatch = candidate;
            }
        }

        // Verificación de seguridad (nunca debería ser null aquí)
        if (bestMatch == null) {
            return new MatchResponse(0.0, "No se encontraron coincidencias válidas");
        }

        String recommendation;
        if (bestScore >= 80) {
            recommendation = "ALTA COINCIDENCIA - POSIBLE MISMA MASCOTA (ID: " + bestMatch.getId() + ")";
        } else if (bestScore >= 50) {
            recommendation = "COINCIDENCIA MEDIA - REVISAR DETALLES (ID: " + bestMatch.getId() + ")";
        } else {
            recommendation = "BAJA COINCIDENCIA - POCA PROBABILIDAD (ID: " + bestMatch.getId() + ")";
        }

        return new MatchResponse(bestScore, recommendation);
    }

    private double calculateScore(PetMatchRequest req, PetData candidate) {
        double score = 0.0;

        // Color (40%)
        if (req.getPetColor() != null && candidate.getColor() != null &&
            req.getPetColor().equalsIgnoreCase(candidate.getColor())) {
            score += 40;
        }

        // Ubicación (20%)
        if (req.getLatitude() != null && req.getLongitude() != null &&
            candidate.getLatitude() != null && candidate.getLongitude() != null) {
            double distance = calculateDistance(
                req.getLatitude(), req.getLongitude(),
                candidate.getLatitude(), candidate.getLongitude()
            );
            if (distance < 10) {
                score += 20;
            } else if (distance < 25) {
                score += 10;
            } else if (distance < 50) {
                score += 5;
            }
        }

        return score;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }

    private List<PetData> getPetsByStatus(String status) {
        try {
            return webClient.get()
                    .uri("/pets/status/" + status)
                    .retrieve()
                    .bodyToFlux(PetData.class)
                    .collectList()
                    .block();
        } catch (Exception e) {
            return null;
        }
    }

    static class PetData {
        private Long id;
        private String type;
        private String breed;
        private String color;
        private Double latitude;
        private Double longitude;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getBreed() { return breed; }
        public void setBreed(String breed) { this.breed = breed; }

        public String getColor() { return color; }
        public void setColor(String color) { this.color = color; }

        public Double getLatitude() { return latitude; }
        public void setLatitude(Double latitude) { this.latitude = latitude; }

        public Double getLongitude() { return longitude; }
        public void setLongitude(Double longitude) { this.longitude = longitude; }
    }
}