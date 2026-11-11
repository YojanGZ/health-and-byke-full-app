package com.healthandbyke.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "ubicacion_taller")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UbicacionTaller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tallerId; 
    
    @Column(nullable = false, length = 100)
    private String municipioTaller;
    
    @Column(length = 100)
    private String barrioTaller;
    
    @Column(nullable = false, length = 200)
    private String direccionTaller;
    
    @Column(length = 100)
    private String nombreMecanico;
    
    @Column(length = 100)
    private String especialidadTaller;
    
    @Column(length = 20)
    private String telefonoTaller; // Añadido por US-09

    public UbicacionTaller() {}

    // Getters y Setters
    public Integer getTallerId() { return tallerId; }
    public void setTallerId(Integer tallerId) { this.tallerId = tallerId; }
    public String getMunicipioTaller() { return municipioTaller; }
    public void setMunicipioTaller(String municipioTaller) { this.municipioTaller = municipioTaller; }
    public String getBarrioTaller() { return barrioTaller; }
    public void setBarrioTaller(String barrioTaller) { this.barrioTaller = barrioTaller; }
    public String getDireccionTaller() { return direccionTaller; }
    public void setDireccionTaller(String direccionTaller) { this.direccionTaller = direccionTaller; }
    public String getNombreMecanico() { return nombreMecanico; }
    public void setNombreMecanico(String nombreMecanico) { this.nombreMecanico = nombreMecanico; }
    public String getEspecialidadTaller() { return especialidadTaller; }
    public void setEspecialidadTaller(String especialidadTaller) { this.especialidadTaller = especialidadTaller; }
    public String getTelefonoTaller() { return telefonoTaller; }
    public void setTelefonoTaller(String telefonoTaller) { this.telefonoTaller = telefonoTaller; }
}
