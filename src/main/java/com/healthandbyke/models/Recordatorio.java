package com.healthandbyke.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "recordatorio")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Recordatorio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recordatorioId; 
    
    @Column(nullable = false, length = 200)
    private String notificacionR;
    
    @Column(length = 50)
    private String estadoRepuesto;
    
    @Column(length = 500)
    private String descripcionRecordatorio;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estado_id", nullable = false)
    private EstadoMoto estadoMoto; // Relación del draw.io

    public Recordatorio() {}

    // Getters y Setters
    public Integer getRecordatorioId() { return recordatorioId; }
    public void setRecordatorioId(Integer recordatorioId) { this.recordatorioId = recordatorioId; }
    public String getNotificacionR() { return notificacionR; }
    public void setNotificacionR(String notificacionR) { this.notificacionR = notificacionR; }
    public String getEstadoRepuesto() { return estadoRepuesto; }
    public void setEstadoRepuesto(String estadoRepuesto) { this.estadoRepuesto = estadoRepuesto; }
    public String getDescripcionRecordatorio() { return descripcionRecordatorio; }
    public void setDescripcionRecordatorio(String descripcionRecordatorio) { this.descripcionRecordatorio = descripcionRecordatorio; }
    public EstadoMoto getEstadoMoto() { return estadoMoto; }
    public void setEstadoMoto(EstadoMoto estadoMoto) { this.estadoMoto = estadoMoto; }
}
