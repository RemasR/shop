package com.example.shop.domain.validators.user;

import com.example.shop.domain.dto.SimpleViolation;
import com.example.shop.domain.validators.Validator;

import java.util.HashSet;
import java.util.Set;

public class UserIdValidator implements Validator<String> {

    @Override
    public Set<SimpleViolation> validate(String id) {
        Set<SimpleViolation> violations = new HashSet<>();

        if (id == null) {
            violations.add(new SimpleViolation("user.id", "User ID cannot be null"));
        }

        return violations;
    }
}
