package com.healthandbyke.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "costo_arreglo")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CostoArreglo {
    @Id
    private Integer arreglosId; 
    
    @Column(nullable = false)
    private double manoDeObra;
    
    @Column(nullable = false)
    private double costoRepuestos;
    
    private double otrosGastos;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name = "arreglos_id")
    private Arreglos arreglos;

    public CostoArreglo() {}

    public double getTotalCosto() {
        return this.manoDeObra + this.costoRepuestos + this.otrosGastos;
    }

    // Getters y Setters
    public Integer getArreglosId() { return arreglosId; }
    public void setArreglosId(Integer arreglosId) { this.arreglosId = arreglosId; }
    public double getManoDeObra() { return manoDeObra; }
    public void setManoDeObra(double manoDeObra) { this.manoDeObra = manoDeObra; }
    public double getCostoRepuestos() { return costoRepuestos; }
    public void setCostoRepuestos(double costoRepuestos) { this.costoRepuestos = costoRepuestos; }
    public double getOtrosGastos() { return otrosGastos; }
    public void setOtrosGastos(double otrosGastos) { this.otrosGastos = otrosGastos; }
    public Arreglos getArreglos() { return arreglos; }
    public void setArreglos(Arreglos arreglos) { this.arreglos = arreglos; }
}
