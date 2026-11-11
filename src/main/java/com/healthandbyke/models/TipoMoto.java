package com.healthandbyke.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "tipo_moto")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TipoMoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tipoMotoId;
    
    @Column(nullable = false, length = 50)
    private String marcaMoto;
    
    @Column(length = 50)
    private String tipoUso;
    
    @Column(length = 50)
    private String categoriaMoto;
    
    @Column(nullable = false, length = 20)
    private String cilindrajeMoto;
    
    @Column(nullable = false, length = 50)
    private String modeloMoto;
    
    @Column(unique = true, nullable = false, length = 20)
    private String placa; // Añadido por US-01

    @Column(nullable = false)
    private Integer odometroActual; // Añadido por US-03

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user; // Relación con el propietario del vehículo

    public TipoMoto() {}

    // Getters y Setters
    public Integer getTipoMotoId() { return tipoMotoId; }
    public void setTipoMotoId(Integer tipoMotoId) { this.tipoMotoId = tipoMotoId; }
    public String getMarcaMoto() { return marcaMoto; }
    public void setMarcaMoto(String marcaMoto) { this.marcaMoto = marcaMoto; }
    public String getTipoUso() { return tipoUso; }
    public void setTipoUso(String tipoUso) { this.tipoUso = tipoUso; }
    public String getCategoriaMoto() { return categoriaMoto; }
    public void setCategoriaMoto(String categoriaMoto) { this.categoriaMoto = categoriaMoto; }
    public String getCilindrajeMoto() { return cilindrajeMoto; }
    public void setCilindrajeMoto(String cilindrajeMoto) { this.cilindrajeMoto = cilindrajeMoto; }
    public String getModeloMoto() { return modeloMoto; }
    public void setModeloMoto(String modeloMoto) { this.modeloMoto = modeloMoto; }
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    public Integer getOdometroActual() { return odometroActual; }
    public void setOdometroActual(Integer odometroActual) { this.odometroActual = odometroActual; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
