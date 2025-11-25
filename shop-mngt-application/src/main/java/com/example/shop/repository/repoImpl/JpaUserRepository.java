package com.example.shop.repository;

import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class JpaUserRepository implements UserRepository {

    private final List<User> users = new ArrayList<>();

    @Override
    public User save(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(user.getId())) {
                users.set(i, user);
                return user;
            }
        }

        users.add(user);
        return user;
    }

    @Override
    public User findById(String id) {
        for (User u : users) {
            if (u.getId().equals(id)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public User findByEmail(String email) {
        for (User u : users) {
            if (u.getEmail().equals(email)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users);
    }

    @Override
    public void deleteById(String id) {
        users.removeIf(user -> user.getId().equals(id));
    }

    @Override
    public boolean existsById(String id) {
        for (User u : users) {
            if (u.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }
}
