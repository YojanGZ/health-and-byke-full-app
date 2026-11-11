package com.healthandbyke.services.impl;

import com.healthandbyke.models.UbicacionTaller;
import com.healthandbyke.repositories.UbicacionTallerRepository;
import com.healthandbyke.services.UbicacionTallerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UbicacionTallerServiceImpl implements UbicacionTallerService {

    @Autowired
    private UbicacionTallerRepository repository;

    @Override
    public void create(UbicacionTaller entity) {
        repository.save(entity);
    }

    @Override
    public List<UbicacionTaller> selectAll() {
        return repository.findAll();
    }

    @Override
    public UbicacionTaller selectById(int id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void update(int id, UbicacionTaller entity) {
        entity.setTallerId(id);
        repository.save(entity);
    }

    @Override
    public void delete(int id) {
        repository.deleteById(id);
    }
}
