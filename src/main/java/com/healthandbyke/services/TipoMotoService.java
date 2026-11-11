package com.healthandbyke.services;

import com.healthandbyke.models.TipoMoto;
import java.util.List;

public interface TipoMotoService {
    void create(TipoMoto entity);
    List<TipoMoto> selectAll();
    TipoMoto selectById(int id);
    void update(int id, TipoMoto entity);
    void delete(int id);
}
