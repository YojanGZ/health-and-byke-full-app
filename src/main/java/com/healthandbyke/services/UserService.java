package com.healthandbyke.services;

import com.healthandbyke.models.User;
import java.util.List;

public interface UserService {
    void create(User entity);
    List<User> selectAll();
    User selectById(int id);
    void update(int id, User entity);
    void delete(int id);
}
