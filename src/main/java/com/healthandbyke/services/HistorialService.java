package com.healthandbyke.services;

import com.healthandbyke.models.Historial;
import java.util.List;

public interface HistorialService {
    void create(Historial entity);
    List<Historial> selectAll();
    Historial selectById(int id);
    void update(int id, Historial entity);
    void delete(int id);
}
