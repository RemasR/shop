package com.example.shop.domain.validators;

import com.example.shop.domain.dto.SimpleViolation;

import java.util.Set;

public interface Validator<T> {
    Set<SimpleViolation> validate(T object) throws IllegalArgumentException;
}