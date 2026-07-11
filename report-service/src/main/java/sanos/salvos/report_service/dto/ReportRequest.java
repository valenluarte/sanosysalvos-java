package sanos.salvos.report_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReportRequest {

    @NotNull(message = "El userId es obligatorio")
    public Long userId;

    @NotNull(message = "El petId es obligatorio")
    public Long petId;

    @NotBlank(message = "El tipo (LOST/FOUND) es obligatorio")
    public String type;

    @NotNull(message = "La latitud es obligatoria")
    public Double latitude;

    @NotNull(message = "La longitud es obligatoria")
    public Double longitude;

    @NotBlank(message = "La ubicación es obligatoria")
    public String lastSeenLocation;
}