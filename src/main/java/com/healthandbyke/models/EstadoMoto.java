package com.healthandbyke.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "estado_moto")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EstadoMoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer estadoId;
    
    @Column(nullable = false, length = 100)
    private String piezasMoto; // Ej: Kit Arrastre, Pastillas (US-10, US-11)
    
    @Column(nullable = false, length = 50)
    private String estadoPiezas;
    
    @Column(length = 200)
    private String reparacionesMoto;
    
    @Column(nullable = false)
    private LocalDate fechaReparacion;
    
    private Integer kilometrosEstimadosUso; // Añadido para desgaste (EPIC-03)

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_moto_id")
    private TipoMoto moto; // Motocicleta a la que pertenece este estado de piezas

    public EstadoMoto() {}

    // Getters y Setters
    public Integer getEstadoId() { return estadoId; }
    public void setEstadoId(Integer estadoId) { this.estadoId = estadoId; }
    public String getPiezasMoto() { return piezasMoto; }
    public void setPiezasMoto(String piezasMoto) { this.piezasMoto = piezasMoto; }
    public String getEstadoPiezas() { return estadoPiezas; }
    public void setEstadoPiezas(String estadoPiezas) { this.estadoPiezas = estadoPiezas; }
    public String getReparacionesMoto() { return reparacionesMoto; }
    public void setReparacionesMoto(String reparacionesMoto) { this.reparacionesMoto = reparacionesMoto; }
    public LocalDate getFechaReparacion() { return fechaReparacion; }
    public void setFechaReparacion(LocalDate fechaReparacion) { this.fechaReparacion = fechaReparacion; }
    public Integer getKilometrosEstimadosUso() { return kilometrosEstimadosUso; }
    public void setKilometrosEstimadosUso(Integer kilometrosEstimadosUso) { this.kilometrosEstimadosUso = kilometrosEstimadosUso; }
    public TipoMoto getMoto() { return moto; }
    public void setMoto(TipoMoto moto) { this.moto = moto; }
}
