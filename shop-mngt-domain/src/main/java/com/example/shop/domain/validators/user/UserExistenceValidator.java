package com.example.shop.domain.validators.user;

import com.example.shop.domain.dto.SimpleViolation;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.validators.Validator;

import java.util.HashSet;
import java.util.Set;

public class UserExistenceValidator implements Validator<String> {

    private final UserRepository userRepository;

    public UserExistenceValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Set<SimpleViolation> validate(String id) {
        Set<SimpleViolation> violations = new HashSet<>();

        if (!userRepository.existsById(id)) {
            violations.add(new SimpleViolation("user.id", "User with ID " + id + " does not exist"));
        }

        return violations;
    }
}
