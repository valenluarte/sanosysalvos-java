package sanos.salvos.matching_service.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PetMatchRequest {

    @NotBlank(message = "El tipo de mascota es obligatorio")
    private String petType;

    private String petColor;
    private String petBreed;

    @NotNull(message = "La latitud es obligatoria")
    private Double latitude;

    @NotNull(message = "La longitud es obligatoria")
    private Double longitude;

    @NotBlank(message = "El tipo de reporte (LOST/FOUND) es obligatorio")
    private String reportType;

    // Getters y Setters
    public String getPetType() { return petType; }
    public void setPetType(String petType) { this.petType = petType; }

    public String getPetColor() { return petColor; }
    public void setPetColor(String petColor) { this.petColor = petColor; }

    public String getPetBreed() { return petBreed; }
    public void setPetBreed(String petBreed) { this.petBreed = petBreed; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }
}