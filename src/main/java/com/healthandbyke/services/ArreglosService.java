package com.healthandbyke.services;

import com.healthandbyke.models.Arreglos;
import java.util.List;

public interface ArreglosService {
    void create(Arreglos entity);
    List<Arreglos> selectAll();
    Arreglos selectById(int id);
    void update(int id, Arreglos entity);
    void delete(int id);
}
