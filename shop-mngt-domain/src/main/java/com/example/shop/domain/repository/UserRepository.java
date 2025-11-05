package com.example.shop.domain.repository;

import com.example.shop.domain.entity.User;
import java.util.*;

public interface UserRepository {

    User save(User user);

    User findById(UUID id);

    User findByEmail(String email);

    List<User> findAll();

    void deleteById(UUID id);

    boolean existsById(UUID id);
}