package com.healthandbyke.services.impl;

import com.healthandbyke.models.Historial;
import com.healthandbyke.repositories.HistorialRepository;
import com.healthandbyke.repositories.RecordatorioRepository;
import com.healthandbyke.repositories.UbicacionTallerRepository;
import com.healthandbyke.services.HistorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HistorialServiceImpl implements HistorialService {

    @Autowired
    private HistorialRepository repository;

    @Autowired
    private RecordatorioRepository recordatorioRepository;

    @Autowired
    private UbicacionTallerRepository ubicacionTallerRepository;

    @Override
    public void create(Historial entity) {
        resolveRelations(entity);
        repository.save(entity);
    }

    @Override
    public List<Historial> selectAll() {
        return repository.findAll();
    }

    @Override
    public Historial selectById(int id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void update(int id, Historial entity) {
        entity.setHistorialId(id);
        resolveRelations(entity);
        repository.save(entity);
    }

    @Override
    public void delete(int id) {
        repository.deleteById(id);
    }

    private void resolveRelations(Historial entity) {
        if (entity.getRecordatorio() != null && entity.getRecordatorio().getRecordatorioId() != null) {
            recordatorioRepository.findById(entity.getRecordatorio().getRecordatorioId()).ifPresent(entity::setRecordatorio);
        }
        if (entity.getTaller() != null && entity.getTaller().getTallerId() != null) {
            ubicacionTallerRepository.findById(entity.getTaller().getTallerId()).ifPresent(t -> {
                entity.setTaller(t);
                if (entity.getNomTaller() == null || entity.getNomTaller().isBlank()) {
                    entity.setNomTaller(t.getMunicipioTaller() + " - " + t.getDireccionTaller());
                }
            });
        }
    }
}
