package com.healthandbyke.services.impl;

import com.healthandbyke.models.Arreglos;
import com.healthandbyke.repositories.ArreglosRepository;
import com.healthandbyke.repositories.TipoMotoRepository;
import com.healthandbyke.services.ArreglosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ArreglosServiceImpl implements ArreglosService {

    @Autowired
    private ArreglosRepository repository;

    @Autowired
    private TipoMotoRepository tipoMotoRepository;

    @Override
    public void create(Arreglos entity) {
        resolveRelations(entity);
        repository.save(entity);
    }

    @Override
    public List<Arreglos> selectAll() {
        return repository.findAll();
    }

    @Override
    public Arreglos selectById(int id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void update(int id, Arreglos entity) {
        entity.setArreglosId(id);
        resolveRelations(entity);
        repository.save(entity);
    }

    @Override
    public void delete(int id) {
        repository.deleteById(id);
    }

    private void resolveRelations(Arreglos entity) {
        if (entity.getMoto() != null && entity.getMoto().getTipoMotoId() != null) {
            tipoMotoRepository.findById(entity.getMoto().getTipoMotoId()).ifPresent(entity::setMoto);
        }
    }
}
