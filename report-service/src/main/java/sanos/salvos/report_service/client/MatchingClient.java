package sanos.salvos.report_service.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import sanos.salvos.report_service.dto.MatchResponse;

import java.util.Map;

@Component
public class MatchingClient {

    private final WebClient webClient;

    public MatchingClient() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8085")
                .build();
    }

    public MatchResponse match(Double lat, Double lon, String type, String petType, String petColor, String petBreed) {
        System.out.println("📤 Enviando a matching-service:");
        System.out.println("  - petType: " + petType);
        System.out.println("  - petColor: " + petColor);
        System.out.println("  - petBreed: " + petBreed);
        System.out.println("  - latitude: " + lat);
        System.out.println("  - longitude: " + lon);
        System.out.println("  - reportType: " + type);

        Map<String, Object> request = Map.of(
                "petType", petType != null ? petType : "",
                "petColor", petColor != null ? petColor : "",
                "petBreed", petBreed != null ? petBreed : "",
                "latitude", lat,
                "longitude", lon,
                "reportType", type
        );

        return webClient.post()
                .uri("/matching")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(MatchResponse.class)
                .block();
    }
}