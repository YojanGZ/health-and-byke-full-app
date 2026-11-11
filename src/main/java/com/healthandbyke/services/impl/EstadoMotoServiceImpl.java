package com.healthandbyke.services.impl;

import com.healthandbyke.models.EstadoMoto;
import com.healthandbyke.repositories.EstadoMotoRepository;
import com.healthandbyke.repositories.TipoMotoRepository;
import com.healthandbyke.services.EstadoMotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EstadoMotoServiceImpl implements EstadoMotoService {

    @Autowired
    private EstadoMotoRepository repository;

    @Autowired
    private TipoMotoRepository tipoMotoRepository;

    @Override
    public void create(EstadoMoto entity) {
        resolveRelations(entity);
        repository.save(entity);
    }

    @Override
    public List<EstadoMoto> selectAll() {
        return repository.findAll();
    }

    @Override
    public EstadoMoto selectById(int id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void update(int id, EstadoMoto entity) {
        entity.setEstadoId(id);
        resolveRelations(entity);
        repository.save(entity);
    }

    @Override
    public void delete(int id) {
        repository.deleteById(id);
    }

    private void resolveRelations(EstadoMoto entity) {
        if (entity.getMoto() != null && entity.getMoto().getTipoMotoId() != null) {
            tipoMotoRepository.findById(entity.getMoto().getTipoMotoId()).ifPresent(entity::setMoto);
        }
    }
}
