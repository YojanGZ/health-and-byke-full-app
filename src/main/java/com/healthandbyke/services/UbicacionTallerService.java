package com.healthandbyke.services;

import com.healthandbyke.models.UbicacionTaller;
import java.util.List;

public interface UbicacionTallerService {
    void create(UbicacionTaller entity);
    List<UbicacionTaller> selectAll();
    UbicacionTaller selectById(int id);
    void update(int id, UbicacionTaller entity);
    void delete(int id);
}
