package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.entity.User;

import java.util.Set;

public interface UserService {
    Set<User> getAllUsers();

    void saveUser(User user);

    User getUser(Integer id);

    void deleteUser(Integer id);

    void update(User user);
    User getUserByUsername(String username);
}
