package com.example.shop.domain.usecase.user;

import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.usecase.ValidationExecutor;

import java.util.UUID;

public class FindUserByIdUsecase {

    private final UserRepository userRepository;
    private final ValidationExecutor<UUID> validationExecutor;

    public FindUserByIdUsecase(UserRepository userRepository, ValidationExecutor<UUID> validationExecutor) {
        this.userRepository = userRepository;
        this.validationExecutor = validationExecutor;
    }

    public User execute(UUID id) {
        validationExecutor.validateAndThrow(id);
        return userRepository.findById(id);
    }
}
