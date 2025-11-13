package com.example.userValidator;

import com.example.shop.domain.dto.SimpleViolation;
import com.example.shop.domain.entity.User;
import com.example.shop.domain.validators.user.PhonenumberValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PhonenumberValidatorTest {

    private PhonenumberValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PhonenumberValidator();
    }

    @Test
    void givenValidPhoneNumber_whenValidate_thenNoViolations() {
        User user = User.builder()
                .name("remas")
                .email("remas@test.com")
                .phoneNumber("+962794583728")
                .build();

        Set<SimpleViolation> violations = validator.validate(user);

        assertTrue(violations.isEmpty());
    }

    @Test
    void givenNullPhoneNumber_whenValidate_thenReturnsViolation() {
        User user = User.builder()
                .name("remas")
                .email("remas@test.com")
                .build();

        Set<SimpleViolation> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());

        SimpleViolation violation = null;
        for (SimpleViolation v : violations) {
            violation = v;
            break;
        }

        assertEquals("user.phoneNumber", violation.getViolator());
        assertTrue(violation.getViolation().contains("cannot be null or empty"));
    }

    @Test
    void givenInvalidPhoneNumber_whenValidate_thenReturnsViolation() {
        User user = User.builder()
                .name("remas")
                .email("remas@test.com")
                .phoneNumber("+s33a")
                .build();

        Set<SimpleViolation> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());

        SimpleViolation violation = null;
        for (SimpleViolation v : violations) {
            violation = v;
            break;
        }

        assertEquals("user.phoneNumber", violation.getViolator());
        assertTrue(violation.getViolation().contains("Invalid"));
    }
}
