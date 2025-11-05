package com.example.shop.domain.usecase.user;

import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.UserRepository;

import java.util.UUID;

public class CreateUserUsecase {
    private final UserRepository userRepository;

    public CreateUserUsecase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(User user) {
        validateName(user);
        validateEmail(user);
        validatePhoneNumber(user);

        User user = new User(UUID.randomUUID(), dto.name, dto.email, dto.phoneNumber);

        return userRepository.save(user);
    }
    private void validateName(User dto) {
        if (dto.name == null || dto.name.length() < 3) throw new IllegalArgumentException("Name too short");
    }

    private void validateEmail(User dto) {
        if (dto.email == null || !dto.email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) throw new IllegalArgumentException("Invalid email");
    }

    private void validatePhoneNumber(User dto) {
        if (dto.phoneNumber == null || !dto.phoneNumber.matches("^\\+9627[7-9]\\d{7}$")) throw new IllegalArgumentException("Invalid phone");
    }
}

