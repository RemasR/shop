package com.example.shop.domain.validators.user;

import com.example.shop.domain.dto.SimpleViolation;
import com.example.shop.domain.validators.Validator;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class UserIdValidator implements Validator<UUID> {

    @Override
    public Set<SimpleViolation> validate(UUID id) {
        Set<SimpleViolation> violations = new HashSet<>();

        if (id == null) {
            violations.add(new SimpleViolation("user.id", "User ID cannot be null"));
        }

        return violations;
    }
}
