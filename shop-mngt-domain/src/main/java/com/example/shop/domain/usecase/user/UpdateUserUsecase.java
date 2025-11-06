package com.example.shop.domain.usecase.user;

import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.UserRepository;

import java.util.UUID;

public class UpdateUserUsecase {
    private final UserRepository userRepository;

    public UpdateUserUsecase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(UUID userId, String name, String email, String phoneNumber) {
        // Find existing user
        User existingUser = userRepository.findById(userId);
        if (existingUser == null) {
            throw new IllegalArgumentException("User not found");
        }

        // Validate new data
        if (name != null) {
            validateName(name);
            existingUser.setName(name);
        }

        if (email != null) {
            validateEmail(email);
            // Check if email is taken by another user
            User userWithEmail = userRepository.findByEmail(email);
            if (userWithEmail != null && !userWithEmail.getId().equals(userId)) {
                throw new IllegalStateException("Email already exists");
            }
            existingUser.setEmail(email);
        }

        if (phoneNumber != null) {
            validatePhoneNumber(phoneNumber);
            existingUser.setPhoneNumber(phoneNumber);
        }

        return userRepository.save(existingUser);
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (name.length() < 3) {
            throw new IllegalArgumentException("Name must be at least 3 characters");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be null or empty");
        }
        if (!phoneNumber.matches("^\\+9627[7-9]\\d{7}$")) {
            throw new IllegalArgumentException("Invalid Jordanian phone number format");
        }
    }
}