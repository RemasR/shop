package com.example.shop.domain.usecase;

import com.example.shop.domain.dto.SimpleViolation;
import com.example.shop.domain.validators.Validator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ValidationExecutor<T> {

    private List<Validator<T>> validators = new ArrayList<>();

    public ValidationExecutor(List<Validator<T>> validators) {
        this.validators.addAll(validators);

    }

    public Set<SimpleViolation> validate(T target, List<Validator<T>> validators) {
        Set<SimpleViolation> violations = new HashSet<>();

        for (Validator<T> validator : validators) {
            violations.addAll(validator.validate(target));
        }
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException("Validation failed: " + violations);
        }
        return violations;
    }
}
