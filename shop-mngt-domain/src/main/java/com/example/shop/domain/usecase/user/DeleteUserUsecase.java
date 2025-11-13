package com.example.shop.domain.usecase.user;

import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.usecase.ValidationExecutor;
import com.example.shop.domain.validators.user.*;

import java.util.UUID;

public class DeleteUserUsecase {

    private final UserRepository userRepository;
    private final ValidationExecutor<UUID> validationExecutor;

    public DeleteUserUsecase(UserRepository userRepository, ValidationExecutor<UUID> validationExecutor) {
        this.userRepository = userRepository;
        this.validationExecutor = validationExecutor;
    }

    public void execute(UUID id) {
        validationExecutor.validateAndThrow(id);
        userRepository.deleteById(id);
    }
}
