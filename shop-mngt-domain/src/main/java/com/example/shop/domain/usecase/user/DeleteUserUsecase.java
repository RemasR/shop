package com.example.shop.domain.usecase.user;

import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.usecase.ValidationExecutor;
import com.example.shop.domain.validators.user.UserExistenceValidator;
import com.example.shop.domain.validators.user.UserIdValidator;

import java.util.List;
import java.util.UUID;

public class DeleteUserUsecase {

    private final UserRepository userRepository;
    private final ValidationExecutor<UUID> validationExecutor;

    public DeleteUserUsecase(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.validationExecutor = new ValidationExecutor<>(
                List.of(
                        new UserIdValidator(),
                        new UserExistenceValidator(userRepository)
                )
        );
    }

    public void execute(UUID id) {
        validationExecutor.validateAndThrow(id);
        userRepository.deleteById(id);
    }
}
