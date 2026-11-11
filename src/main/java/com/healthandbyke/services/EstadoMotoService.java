package com.healthandbyke.services;

import com.healthandbyke.models.EstadoMoto;
import java.util.List;

public interface EstadoMotoService {
    void create(EstadoMoto entity);
    List<EstadoMoto> selectAll();
    EstadoMoto selectById(int id);
    void update(int id, EstadoMoto entity);
    void delete(int id);
}
