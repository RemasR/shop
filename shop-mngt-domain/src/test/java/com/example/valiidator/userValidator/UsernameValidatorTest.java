package com.example.valiidator.userValidator;

import com.example.shop.domain.dto.SimpleViolation;
import com.example.shop.domain.model.User;
import com.example.shop.domain.validators.user.UsernameValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UsernameValidatorTest {

    private UsernameValidator validator;

    @BeforeEach
    void setUp() {
        validator = new UsernameValidator();
    }

    @Test
    void givenValidName_whenValidate_thenNoViolations() {
        User user = User.builder()
                .name("remas")
                .email("remas@test.com")
                .phoneNumber("+962794583728")
                .build();

        Set<SimpleViolation> violations = validator.validate(user);

        assertTrue(violations.isEmpty());
    }

    @Test
    void givenNullName_whenValidate_thenReturnsViolation() {
        User user = User.builder()
                .email("remas@test.com")
                .phoneNumber("+962794583728")
                .build();

        Set<SimpleViolation> violations = validator.validate(user);

        assertFalse(violations.isEmpty());

        assertEquals(1, violations.size());

        SimpleViolation violation = null;
        for (SimpleViolation v : violations) {
            violation = v;
            break;
        }

        assertEquals("user.name", violation.getViolator());
        assertTrue(violation.getViolation().contains("cannot be null or empty"));
    }
}