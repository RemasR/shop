package com.example.shop.domain.usecase.user;

import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.UserRepository;

import java.util.UUID;

public class FindUserByIdUsecase {
    private final UserRepository userRepository;

    public FindUserByIdUsecase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        User user = userRepository.findById(userId);

        if (user == null) {
            throw new IllegalArgumentException("User with ID " + userId + " not found");
        }

        return user;
    }
}