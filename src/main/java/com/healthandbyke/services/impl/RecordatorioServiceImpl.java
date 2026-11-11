package com.healthandbyke.services.impl;

import com.healthandbyke.models.Recordatorio;
import com.healthandbyke.repositories.EstadoMotoRepository;
import com.healthandbyke.repositories.RecordatorioRepository;
import com.healthandbyke.services.RecordatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RecordatorioServiceImpl implements RecordatorioService {

    @Autowired
    private RecordatorioRepository repository;

    @Autowired
    private EstadoMotoRepository estadoMotoRepository;

    @Override
    public void create(Recordatorio entity) {
        resolveRelations(entity);
        repository.save(entity);
    }

    @Override
    public List<Recordatorio> selectAll() {
        return repository.findAll();
    }

    @Override
    public Recordatorio selectById(int id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void update(int id, Recordatorio entity) {
        entity.setRecordatorioId(id);
        resolveRelations(entity);
        repository.save(entity);
    }

    @Override
    public void delete(int id) {
        repository.deleteById(id);
    }

    private void resolveRelations(Recordatorio entity) {
        if (entity.getEstadoMoto() != null && entity.getEstadoMoto().getEstadoId() != null) {
            estadoMotoRepository.findById(entity.getEstadoMoto().getEstadoId()).ifPresent(entity::setEstadoMoto);
        }
    }
}
