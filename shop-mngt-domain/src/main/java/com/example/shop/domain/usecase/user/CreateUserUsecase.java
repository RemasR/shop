package com.example.shop.domain.usecase.user;

import com.example.shop.domain.dto.SimpleViolation;
import com.example.shop.domain.dto.UserDTO;
import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.validators.Validator;
import com.example.shop.domain.validators.user.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class CreateUserUsecase {
    private final UserRepository userRepository;
    private final List<Validator<User>> userValidators;

    public CreateUserUsecase(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userValidators = List.of(
                new UsernameValidator(),
                new EmailValidator(),
                new PhonenumberValidator()
        );
    }

    public User execute(UserDTO dto) {
        User user = new User(
                UUID.randomUUID(),
                dto.getName(),
                dto.getEmail(),
                dto.getPhoneNumber()
        );

        Set<SimpleViolation> violations = new HashSet<>();
        for (Validator<User> validator : userValidators) {
            violations.addAll(validator.validate(user));
        }
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException("Validation failed: " + violations);
        }
        return userRepository.save(user);
    }
}