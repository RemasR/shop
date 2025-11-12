package com.example.userValidator;

import com.example.shop.domain.dto.SimpleViolation;
import com.example.shop.domain.entity.User;
import com.example.shop.domain.validators.user.PhonenumberValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PhonenumberValidatorTest {

    private PhonenumberValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PhonenumberValidator();
    }

    @Test
    void givenValidPhoneNumber_whenValidate_thenNoViolations() {
        User user = new User(UUID.randomUUID(), "Remas", "remas@test.com", "+962794583728");
        Set<SimpleViolation> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    void givenNullPhoneNumber_whenValidate_thenReturnsViolation() {
        User user = new User(UUID.randomUUID(), "Remas", "remas@test.com", null);
        Set<SimpleViolation> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        SimpleViolation violation = violations.iterator().next();
        assertEquals("user.phoneNumber", violation.getViolator());
        assertTrue(violation.getViolation().contains("cannot be null or empty"));
    }

    @Test
    void givenInvalidPhoneNumber_whenValidate_thenReturnsViolation() {
        User user = new User(UUID.randomUUID(), "Remas", "remas@test.com", "123abc");
        Set<SimpleViolation> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        SimpleViolation violation = violations.iterator().next();
        assertEquals("user.phoneNumber", violation.getViolator());
        assertTrue(violation.getViolation().contains("Invalid"));
    }
}
