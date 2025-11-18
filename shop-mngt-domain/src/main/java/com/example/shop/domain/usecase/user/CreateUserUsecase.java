package com.example.shop.domain.usecase.user;

import com.example.shop.domain.dto.UserDTO;
import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.usecase.ValidationExecutor;

import java.util.UUID;

public class CreateUserUsecase {
    private final UserRepository userRepository;
    private final ValidationExecutor<UserDTO> validationExecutor;

    public CreateUserUsecase(UserRepository userRepository, ValidationExecutor<UserDTO> validationExecutor) {
        this.userRepository = userRepository;
        this.validationExecutor = validationExecutor;
    }

    public User execute(UserDTO dto) {
        validationExecutor.validateAndThrow(dto);

        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .name(dto.getName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .build();


        return userRepository.save(user);
    }
}
