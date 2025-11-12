package com.example.shop.domain.validators.user;

import com.example.shop.domain.dto.SimpleViolation;
import com.example.shop.domain.entity.User;
import com.example.shop.domain.usecase.ValidationException;
import com.example.shop.domain.validators.Validator;
import com.example.shop.domain.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

public class EmailValidator implements Validator<User> {

    private final UserRepository userRepository;

    public EmailValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Set<SimpleViolation> validate(User user) {
        Set<SimpleViolation> violations = new HashSet<>();

        String email = user.getEmail();
        if (email == null || email.trim().isEmpty()) {
            violations.add(new SimpleViolation("user.email", "Email cannot be null or empty"));
        } else if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            violations.add(new SimpleViolation("user.email", "Invalid email format"));
        } else {
            User existing = userRepository.findByEmail(email);
            if (existing != null && !existing.getId().equals(user.getId())) {
                violations.add(new SimpleViolation("user.email", "Email already exists"));
            }
        }

        return violations;
    }
}
