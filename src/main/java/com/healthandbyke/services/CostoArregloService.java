package com.healthandbyke.services;

import com.healthandbyke.models.CostoArreglo;
import java.util.List;

public interface CostoArregloService {
    void create(CostoArreglo entity);
    List<CostoArreglo> selectAll();
    CostoArreglo selectById(int id);
    void update(int id, CostoArreglo entity);
    void delete(int id);
}
