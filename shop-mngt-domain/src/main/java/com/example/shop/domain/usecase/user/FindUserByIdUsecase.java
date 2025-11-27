package com.example.shop.domain.usecase.user;

import com.example.shop.domain.model.User;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.usecase.ValidationExecutor;

public class FindUserByIdUsecase {

    private final UserRepository userRepository;
    private final ValidationExecutor<String> validationExecutor;

    public FindUserByIdUsecase(UserRepository userRepository, ValidationExecutor<String> validationExecutor) {
        this.userRepository = userRepository;
        this.validationExecutor = validationExecutor;
    }

    public User execute(String id) {
        validationExecutor.validateAndThrow(id);
        return userRepository.findById(id);
    }
}
