package com.example.shop.domain.usecase.user;

import com.example.shop.domain.dto.SimpleViolation;
import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.usecase.ValidationException;
import com.example.shop.domain.validators.Validator;

import java.util.HashSet;
import java.util.Set;

public class EmailUniquenessValidator implements Validator<User> {

    private final UserRepository userRepository;

    public EmailUniquenessValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Set<SimpleViolation> validate(User user) {
        Set<SimpleViolation> violations = new HashSet<>();

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            violations.add(new SimpleViolation("user.email", "Email cannot be null or empty"));
        } else {
            User existing = userRepository.findByEmail(user.getEmail());
            if (existing != null && !existing.getId().equals(user.getId())) {
                violations.add(new SimpleViolation("user.email", "Email already exists"));
            }
        }

        if (!violations.isEmpty()) {
            throw new ValidationException(violations);
        }

        return violations;
    }
}
