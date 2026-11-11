package com.healthandbyke.services.impl;

import com.healthandbyke.models.User;
import com.healthandbyke.repositories.UserRepository;
import com.healthandbyke.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Override
    public void create(User entity) {
        repository.save(entity);
    }

    @Override
    public List<User> selectAll() {
        return repository.findAll();
    }

    @Override
    public User selectById(int id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void update(int id, User entity) {
        entity.setUserId(id);
        repository.save(entity);
    }

    @Override
    public void delete(int id) {
        repository.deleteById(id);
    }
}
