package com.healthandbyke.services;

import com.healthandbyke.models.Recordatorio;
import java.util.List;

public interface RecordatorioService {
    void create(Recordatorio entity);
    List<Recordatorio> selectAll();
    Recordatorio selectById(int id);
    void update(int id, Recordatorio entity);
    void delete(int id);
}
