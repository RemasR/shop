package com.example.shop.domain.usecase.user;

import com.example.shop.domain.dto.UserDTO;
import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.UserRepository;
import java.util.UUID;

public class CreateUserUsecase {
    private final UserRepository userRepository;

    public CreateUserUsecase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(UserDTO dto) {

        validateName(dto.getName());
        validateEmail(dto.getEmail());
        validatePhoneNumber(dto.getPhoneNumber());

        if (userRepository.findByEmail(dto.getEmail()) != null) {
            throw new IllegalStateException("Email already exists");
        }

        User newUser = new User(UUID.randomUUID(), dto.getName(), dto.getEmail(), dto.getPhoneNumber());

        return userRepository.save(newUser);
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