package com.example.shop.domain.usecase.user;

import com.example.shop.domain.dto.UserDTO;
import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.UserRepository;

public class FindUserByEmailUsecase {
    private final UserRepository userRepository;

    public FindUserByEmailUsecase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(UserDTO dto) {
        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        if (!dto.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        User user = userRepository.findByEmail(dto.getEmail());

        if (user == null) {
            throw new IllegalArgumentException("User with email " + dto.getEmail() + " not found");
        }

        return user;
    }
}