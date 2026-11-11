package com.healthandbyke.services.impl;

import com.healthandbyke.models.TipoMoto;
import com.healthandbyke.repositories.TipoMotoRepository;
import com.healthandbyke.repositories.UserRepository;
import com.healthandbyke.services.TipoMotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TipoMotoServiceImpl implements TipoMotoService {

    @Autowired
    private TipoMotoRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void create(TipoMoto entity) {
        resolveRelations(entity);
        repository.save(entity);
    }

    @Override
    public List<TipoMoto> selectAll() {
        return repository.findAll();
    }

    @Override
    public TipoMoto selectById(int id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void update(int id, TipoMoto entity) {
        entity.setTipoMotoId(id);
        resolveRelations(entity);
        repository.save(entity);
    }

    @Override
    public void delete(int id) {
        repository.deleteById(id);
    }

    private void resolveRelations(TipoMoto entity) {
        if (entity.getUser() != null && entity.getUser().getUserId() != null) {
            userRepository.findById(entity.getUser().getUserId()).ifPresent(entity::setUser);
        }
    }
}
