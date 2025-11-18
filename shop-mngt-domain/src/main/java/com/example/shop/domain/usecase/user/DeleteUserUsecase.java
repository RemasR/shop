package com.example.shop.domain.usecase.user;

import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.usecase.ValidationExecutor;

public class DeleteUserUsecase {

    private final UserRepository userRepository;
    private final ValidationExecutor<String> validationExecutor;

    public DeleteUserUsecase(UserRepository userRepository, ValidationExecutor<String> validationExecutor) {
        this.userRepository = userRepository;
        this.validationExecutor = validationExecutor;
    }

    public void execute(String id) {
        validationExecutor.validateAndThrow(id);
        userRepository.deleteById(id);
    }
}
