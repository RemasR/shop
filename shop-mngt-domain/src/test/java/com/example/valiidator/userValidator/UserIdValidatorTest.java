package com.example.valiidator.userValidator;

import com.example.shop.domain.dto.SimpleViolation;
import com.example.shop.domain.validators.user.UserIdValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserIdValidatorTest {

    private UserIdValidator validator;

    @BeforeEach
    void setUp() {
        validator = new UserIdValidator();
    }

    @Test
    void givenNonNullId_whenValidate_thenNoViolations() {
        UUID id = UUID.randomUUID();

        Set<SimpleViolation> violations = validator.validate(id);

        assertTrue(violations.isEmpty());
    }

    @Test
    void givenNullId_whenValidate_thenReturnsViolation() {
        Set<SimpleViolation> violations = validator.validate(null);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());

        SimpleViolation violation = null;
        for (SimpleViolation v : violations) {
            violation = v;
            break;
        }

        assertEquals("user.id", violation.getViolator());
        assertTrue(violation.getViolation().contains("cannot be null"));
    }
}
