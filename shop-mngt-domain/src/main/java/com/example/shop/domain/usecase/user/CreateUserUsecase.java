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
        if (userRepository.findByEmail(dto.getEmail()) != null) {
            throw new IllegalStateException("Email already exists");
        }

        User newUser = new User(
                UUID.randomUUID(),
                dto.getName(),
                dto.getEmail(),
                dto.getPhoneNumber()
        );

        return userRepository.save(newUser);
    }

}