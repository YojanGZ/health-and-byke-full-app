package com.healthandbyke.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "arreglos")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Arreglos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer arreglosId;
    
    @Column(nullable = false)
    private LocalDate fechaArreglo;
    
    @Column(length = 500)
    private String descripcionArreglos;
    
    @Column(nullable = false, length = 50)
    private String estadoArreglo;

    @Column(nullable = false)
    private Integer kilometrajeMomento; // Añadido por US-06

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_moto_id", nullable = false)
    private TipoMoto moto;

    public Arreglos() {}

    // Getters y Setters
    public Integer getArreglosId() { return arreglosId; }
    public void setArreglosId(Integer arreglosId) { this.arreglosId = arreglosId; }
    public LocalDate getFechaArreglo() { return fechaArreglo; }
    public void setFechaArreglo(LocalDate fechaArreglo) { this.fechaArreglo = fechaArreglo; }
    public String getDescripcionArreglos() { return descripcionArreglos; }
    public void setDescripcionArreglos(String descripcionArreglos) { this.descripcionArreglos = descripcionArreglos; }
    public String getEstadoArreglo() { return estadoArreglo; }
    public void setEstadoArreglo(String estadoArreglo) { this.estadoArreglo = estadoArreglo; }
    public Integer getKilometrajeMomento() { return kilometrajeMomento; }
    public void setKilometrajeMomento(Integer kilometrajeMomento) { this.kilometrajeMomento = kilometrajeMomento; }
    public TipoMoto getMoto() { return moto; }
    public void setMoto(TipoMoto moto) { this.moto = moto; }
}
