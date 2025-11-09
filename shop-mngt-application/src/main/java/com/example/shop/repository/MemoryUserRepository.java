package com.example.shop.repository;

import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.UserRepository;

import java.util.*;

public class MemoryUserRepository implements UserRepository {

    private final Map<UUID, User> userStore = new HashMap<>();
    private final Map<String, UUID> emailIndex = new HashMap<>();

    @Override
    public User save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        UUID existingUserId = emailIndex.get(user.getEmail());
        if (existingUserId != null && !existingUserId.equals(user.getId())) {
            throw new IllegalStateException("Email already exists for another user");
        }

        userStore.put(user.getId(), user);
        emailIndex.put(user.getEmail(), user.getId());
        return user;
    }

    @Override
    public User findById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return userStore.get(id);
    }

    @Override
    public User findByEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        UUID userId = emailIndex.get(email);
        if(userId != null)
            return userStore.get(userId);
        else return null;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userStore.values());
    }

    @Override
    public void deleteById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        User user = userStore.remove(id);
        if (user != null) {
            emailIndex.remove(user.getEmail());
        }
    }

    @Override
    public boolean existsById(UUID id) {
        if (id == null) {
            return false;
        }
        return userStore.containsKey(id);
    }
}
