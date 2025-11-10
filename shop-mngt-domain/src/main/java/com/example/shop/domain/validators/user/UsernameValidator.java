package com.example.shop.domain.validators.user;

import com.example.shop.domain.dto.SimpleViolation;
import com.example.shop.domain.entity.User;
import com.example.shop.domain.validators.Validator;

import java.util.HashSet;
import java.util.Set;

public class UsernameValidator implements Validator<User> {

    @Override
    public Set<SimpleViolation> validate(User user) throws IllegalArgumentException {
        Set<SimpleViolation> violations = new HashSet<>();
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            violations.add(new SimpleViolation("user.name","Name cannot be null or empty"));
        } else if (user.getName().length() < 3) {
            violations.add(new SimpleViolation("user.name","Name must be at least 3 characters"));
        }
        return violations;
    }
}