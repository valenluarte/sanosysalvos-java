package sanos.salvos.pet_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "El tipo es obligatorio para el matching")
    @Column(nullable = false)
    private String type;

    @NotBlank(message = "La raza es obligatoria para el matching")
    @Column(nullable = false)
    private String breed;

    @NotBlank(message = "El color es obligatorio para el matching")
    @Column(nullable = false)
    private String color;

    private String status;

    @NotNull(message = "La latitud es obligatoria para el matching")
    @Column(nullable = false)
    private Double latitude;

    @NotNull(message = "La longitud es obligatoria para el matching")
    @Column(nullable = false)
    private Double longitude;

    @NotNull(message = "El userId es obligatorio")
    @Column(nullable = false)
    private Long userId;

    private String locationAddress;

    public Pet() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getLocationAddress() { return locationAddress; }
    public void setLocationAddress(String locationAddress) { this.locationAddress = locationAddress; }
}