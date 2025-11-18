package com.example.shop.domain.repository;

import com.example.shop.domain.entity.User;

import java.util.List;

public interface UserRepository {

    User save(User user);

    User findById(String id);

    User findByEmail(String email);

    List<User> findAllUsers();

    void deleteById(String id);

    boolean existsById(String id);
}