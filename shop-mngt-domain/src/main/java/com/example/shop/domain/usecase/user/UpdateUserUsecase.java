package com.example.shop.domain.usecase.user;

import com.example.shop.domain.dto.SimpleViolation;
import com.example.shop.domain.dto.UserDTO;
import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.validators.Validator;
import com.example.shop.domain.validators.user.*;

import java.util.*;

public class UpdateUserUsecase {

    private final UserRepository userRepository;
    private final List<Validator<User>> userValidators;
    private final List<Validator<UUID>> idValidators;

    public UpdateUserUsecase(UserRepository userRepository) {
        this.userRepository = userRepository;

        this.userValidators = List.of(
                new UsernameValidator(),
                new EmailValidator(),
                new PhonenumberValidator(),
                new EmailUniquenessValidator(userRepository)
        );

        this.idValidators = List.of(
                new UserIdValidator(),
                new UserExistenceValidator(userRepository)
        );
    }

    public User execute(UUID userId, UserDTO dto) {
        validateAll(idValidators, userId);

        if (dto == null) {
            throw new IllegalArgumentException("Update data cannot be null");
        }

        User existingUser = userRepository.findById(userId);

        if (dto.getName() != null) {
            existingUser.setName(dto.getName());
        }

        if (dto.getEmail() != null) {
            existingUser.setEmail(dto.getEmail());
        }

        if (dto.getPhoneNumber() != null) {
            existingUser.setPhoneNumber(dto.getPhoneNumber());
        }

        validateAll(userValidators, existingUser);

        return userRepository.save(existingUser);
    }

    private <T> void validateAll(List<Validator<T>> validators, T value) {
        Set<SimpleViolation> violations = new HashSet<>();

        for (Validator<T> validator : validators) {
            violations.addAll(validator.validate(value));
        }

        if (!violations.isEmpty()) {
            throw new IllegalArgumentException("Validation failed: " + violations);
        }
    }
}
