package com.example.valiidator.userValidator;

import com.example.shop.domain.dto.SimpleViolation;
import com.example.shop.domain.model.User;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.validators.user.EmailValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class EmailValidatorTest {

    private EmailValidator validator;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        validator = new EmailValidator(userRepository = mock(UserRepository.class));
    }

    @Test
    void givenValidEmail_whenValidate_thenNoViolations() {
        User user = User.builder()
                .name("remas")
                .email("remas@test.com")
                .phoneNumber("+962794583728")
                .build();

        Set<SimpleViolation> violations = validator.validate(user);

        assertTrue(violations.isEmpty());
    }

    @Test
    void givenNullEmail_whenValidate_thenReturnsViolation() {
        User user = User.builder()
                .name("remas")
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

        assertEquals("user.email", violation.getViolator());
        assertTrue(violation.getViolation().contains("cannot be null or empty"));
    }

    @Test
    void givenInvalidEmail_whenValidate_thenReturnsViolation() {
        User user = User.builder()
                .name("remas")
                .email("invalid-email")
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

        assertEquals("user.email", violation.getViolator());
        assertTrue(violation.getViolation().contains("Invalid"));
    }
}
