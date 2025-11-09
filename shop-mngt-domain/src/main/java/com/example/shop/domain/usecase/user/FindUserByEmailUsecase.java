package com.example.shop.domain.usecase.user;

import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.UserRepository;

public class FindUserByEmailUsecase {
    private final UserRepository userRepository;

    public FindUserByEmailUsecase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new IllegalArgumentException("User with email " + email + " not found");
        }

        return user;
    }
}