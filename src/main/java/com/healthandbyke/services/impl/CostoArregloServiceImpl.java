package com.healthandbyke.services.impl;

import com.healthandbyke.models.CostoArreglo;
import com.healthandbyke.repositories.ArreglosRepository;
import com.healthandbyke.repositories.CostoArregloRepository;
import com.healthandbyke.services.CostoArregloService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CostoArregloServiceImpl implements CostoArregloService {

    @Autowired
    private CostoArregloRepository repository;

    @Autowired
    private ArreglosRepository arreglosRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public void create(CostoArreglo entity) {
        resolveRelations(entity, null);
        if (entity.getArreglosId() != null && entityManager.find(CostoArreglo.class, entity.getArreglosId()) != null) {
            entityManager.merge(entity);
        } else {
            entityManager.persist(entity);
        }
    }

    @Override
    public List<CostoArreglo> selectAll() {
        return repository.findAll();
    }

    @Override
    public CostoArreglo selectById(int id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public void update(int id, CostoArreglo entity) {
        resolveRelations(entity, id);
        CostoArreglo existing = entityManager.find(CostoArreglo.class, id);
        if (existing != null) {
            existing.setManoDeObra(entity.getManoDeObra());
            existing.setCostoRepuestos(entity.getCostoRepuestos());
            existing.setOtrosGastos(entity.getOtrosGastos());
            if (entity.getArreglos() != null) {
                existing.setArreglos(entity.getArreglos());
            }
            entityManager.merge(existing);
        } else {
            entityManager.persist(entity);
        }
    }

    @Transactional
    @Override
    public void delete(int id) {
        CostoArreglo existing = entityManager.find(CostoArreglo.class, id);
        if (existing != null) {
            entityManager.remove(existing);
        }
    }

    private void resolveRelations(CostoArreglo entity, Integer fallbackId) {
        Integer targetId = null;
        if (entity.getArreglos() != null && entity.getArreglos().getArreglosId() != null) {
            targetId = entity.getArreglos().getArreglosId();
        } else if (entity.getArreglosId() != null) {
            targetId = entity.getArreglosId();
        } else if (fallbackId != null) {
            targetId = fallbackId;
        }

        if (targetId != null) {
            arreglosRepository.findById(targetId).ifPresent(a -> {
                entity.setArreglos(a);
                entity.setArreglosId(a.getArreglosId());
            });
        }
    }
}
