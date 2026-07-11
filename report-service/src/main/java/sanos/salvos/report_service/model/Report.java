package sanos.salvos.report_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El petId es obligatorio")
    @Column(nullable = false)
    private Long petId;

    @NotNull(message = "El userId es obligatorio")
    @Column(nullable = false)
    private Long userId;

    @NotBlank(message = "El tipo de reporte (LOST/FOUND) es obligatorio")
    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String status = "ACTIVE";

    @NotBlank(message = "La ubicación es obligatoria")
    @Column(nullable = false)
    private String location;

    @NotNull(message = "La latitud es obligatoria")
    @Column(nullable = false)
    private Double latitude;

    @NotNull(message = "La longitud es obligatoria")
    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPetId() { return petId; }
    public void setPetId(Long petId) { this.petId = petId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}