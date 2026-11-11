package com.healthandbyke.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "historial")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Historial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer historialId;
    
    @Column(nullable = false)
    private LocalDate fechaHistorial;
    
    @Column(length = 100)
    private String nomTaller;
    
    @Column(length = 100)
    private String tipoMantenimiento;
    
    @Column(length = 500)
    private String descripcionMantenimiento;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recordatorio_id", nullable = false)
    private Recordatorio recordatorio; // Relación del draw.io

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "taller_id")
    private UbicacionTaller taller; // Taller asignado al mantenimiento

    public Historial() {}

    // Getters y Setters
    public Integer getHistorialId() { return historialId; }
    public void setHistorialId(Integer historialId) { this.historialId = historialId; }
    public LocalDate getFechaHistorial() { return fechaHistorial; }
    public void setFechaHistorial(LocalDate fechaHistorial) { this.fechaHistorial = fechaHistorial; }
    public String getNomTaller() { return nomTaller; }
    public void setNomTaller(String nomTaller) { this.nomTaller = nomTaller; }
    public String getTipoMantenimiento() { return tipoMantenimiento; }
    public void setTipoMantenimiento(String tipoMantenimiento) { this.tipoMantenimiento = tipoMantenimiento; }
    public String getDescripcionMantenimiento() { return descripcionMantenimiento; }
    public void setDescripcionMantenimiento(String descripcionMantenimiento) { this.descripcionMantenimiento = descripcionMantenimiento; }
    public Recordatorio getRecordatorio() { return recordatorio; }
    public void setRecordatorio(Recordatorio recordatorio) { this.recordatorio = recordatorio; }
    public UbicacionTaller getTaller() { return taller; }
    public void setTaller(UbicacionTaller taller) { this.taller = taller; }
}
