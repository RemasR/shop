package com.example.shop.domain.usecase.user;

import com.example.shop.domain.dto.UpdateUserDTO;
import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.UserRepository;

import java.util.UUID;

public class UpdateUserUsecase {
    private final UserRepository userRepository;

    public UpdateUserUsecase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(UUID userId, UpdateUserDTO dto) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (dto == null) {
            throw new IllegalArgumentException("Update data cannot be null");
        }

        User existingUser = userRepository.findById(userId);
        if (existingUser == null) {
            throw new IllegalArgumentException("User not found");
        }

        if (dto.getName() != null) {
            validateName(dto.getName());
            existingUser.setName(dto.getName());
        }

        if (dto.getEmail() != null) {
            validateEmail(dto.getEmail());
            User userWithEmail = userRepository.findByEmail(dto.getEmail());
            if (userWithEmail != null && !userWithEmail.getId().equals(userId)) {
                throw new IllegalStateException("Email already exists");
            }
            existingUser.setEmail(dto.getEmail());
        }

        if (dto.getPhoneNumber() != null) {
            validatePhoneNumber(dto.getPhoneNumber());
            existingUser.setPhoneNumber(dto.getPhoneNumber());
        }

        return userRepository.save(existingUser);
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (name.length() < 3) {
            throw new IllegalArgumentException("Name too short");
        }
    }

    private void validateEmail(String email) {
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email");
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (!phoneNumber.matches("^\\+9627[7-9]\\d{7}$")) {
            throw new IllegalArgumentException("Invalid phone");
        }
    }
}