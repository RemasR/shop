package com.example.shop.domain.usecase.user;

import com.example.shop.domain.dto.SimpleViolation;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.validators.Validator;
import com.example.shop.domain.validators.user.UserExistenceValidator;
import com.example.shop.domain.validators.user.UserIdValidator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class DeleteUserUsecase {

    private final UserRepository userRepository;
    private final List<Validator<UUID>> validators;

    public DeleteUserUsecase(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.validators = List.of(
                new UserIdValidator(),
                new UserExistenceValidator(userRepository)
        );
    }

    public void execute(UUID id) {
        Set<SimpleViolation> violations = new HashSet<>();

        for (Validator<UUID> validator : validators) {
            violations.addAll(validator.validate(id));
        }

        if (!violations.isEmpty()) {
            throw new IllegalArgumentException("Validation failed: " + violations);
        }

        userRepository.deleteById(id);
    }
}
