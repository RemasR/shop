package com.example.shop.domain.validators.user;

import com.example.shop.domain.dto.SimpleViolation;
import com.example.shop.domain.entity.User;
import com.example.shop.domain.validators.Validator;

import java.util.HashSet;
import java.util.Set;

public class EmailValidator implements Validator<User> {

    @Override
    public Set<SimpleViolation> validate(User user) throws IllegalArgumentException {
        Set<SimpleViolation> violations = new HashSet<>();

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            violations.add(new SimpleViolation("user.email","Email cannot be null or empty"));
        } else if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            violations.add(new SimpleViolation("user.email","Invalid user.getEmail() format"));
        }
        return violations;
    }
}