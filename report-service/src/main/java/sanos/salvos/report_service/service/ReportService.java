package sanos.salvos.report_service.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import sanos.salvos.report_service.client.MatchingClient;
import sanos.salvos.report_service.dto.MatchResponse;
import sanos.salvos.report_service.dto.ReportRequest;
import sanos.salvos.report_service.dto.ReportResponse;
import sanos.salvos.report_service.model.Report;
import sanos.salvos.report_service.repository.ReportRepository;

import java.util.List;

@Service
public class ReportService {

    private final ReportRepository repository;
    private final MatchingClient matchingClient;
    private final WebClient webClient;

    public ReportService(ReportRepository repository, MatchingClient matchingClient) {
        this.repository = repository;
        this.matchingClient = matchingClient;
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8083")
                .build();
    }

    public ReportResponse process(ReportRequest request) {
        if (request.userId == null) {
            throw new IllegalArgumentException("El campo userId es obligatorio");
        }
        if (request.petId == null) {
            throw new IllegalArgumentException("El campo petId es obligatorio");
        }
        if (request.type == null || request.type.isEmpty()) {
            throw new IllegalArgumentException("El campo type (LOST/FOUND) es obligatorio");
        }
        if (request.latitude == null) {
            throw new IllegalArgumentException("La latitud es obligatoria");
        }
        if (request.longitude == null) {
            throw new IllegalArgumentException("La longitud es obligatoria");
        }

        // 1. Obtener datos de la mascota desde pet-service
        PetData pet = getPetData(request.petId);

        // 2. Crear y guardar reporte
        Report report = new Report();
        report.setPetId(request.petId);
        report.setUserId(request.userId);
        report.setType(request.type);
        report.setStatus("ACTIVE");
        report.setLocation(request.lastSeenLocation);
        report.setLatitude(request.latitude);
        report.setLongitude(request.longitude);

        Report saved = repository.save(report);

        // 3. Ejecutar matching con datos REALES de la mascota
        MatchResponse match = matchingClient.match(
            request.latitude,
            request.longitude,
            request.type,
            pet.getType(),
            pet.getColor(),
            pet.getBreed()
        );

        return new ReportResponse(saved.getId(), saved.getStatus(), match);
    }

    public List<Report> getAllReports() {
        return repository.findAll();
    }

    public Report getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado con ID: " + id));
    }

    public List<Report> getReportsByPetId(Long petId) {
        return repository.findByPetId(petId);
    }

    public List<Report> getReportsByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    public Report update(Long id, Report newData) {
        Report report = getById(id);
        report.setType(newData.getType());
        report.setStatus(newData.getStatus());
        report.setLocation(newData.getLocation());
        report.setLatitude(newData.getLatitude());
        report.setLongitude(newData.getLongitude());
        return repository.save(report);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Reporte no encontrado con ID: " + id);
        }
        repository.deleteById(id);
    }

    // Obtener datos de la mascota desde pet-service
    private PetData getPetData(Long petId) {
        try {
            System.out.println("🔍 Obteniendo datos de mascota con ID: " + petId + " desde pet-service");
            PetData pet = webClient.get()
                    .uri("/pets/" + petId)
                    .retrieve()
                    .bodyToMono(PetData.class)
                    .block();
            System.out.println("✅ Datos obtenidos: " + pet);
            return pet;
        } catch (Exception e) {
            System.err.println("❌ Error al obtener datos de la mascota: " + e.getMessage());
            throw new RuntimeException("Error al obtener datos de la mascota desde pet-service: " + e.getMessage());
        }
    }

    // Clase interna para mapear la respuesta de pet-service
    static class PetData {
        private Long id;
        private String type;
        private String breed;
        private String color;
        private String status;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getBreed() { return breed; }
        public void setBreed(String breed) { this.breed = breed; }

        public String getColor() { return color; }
        public void setColor(String color) { this.color = color; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        @Override
        public String toString() {
            return "PetData{id=" + id + ", type='" + type + "', breed='" + breed + "', color='" + color + "', status='" + status + "'}";
        }
    }
}