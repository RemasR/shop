package com.example.valiidator.userValidator;

import com.example.shop.domain.dto.SimpleViolation;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.validators.user.UserExistenceValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserExistenceValidatorTest {

    private UserRepository userRepository;
    private UserExistenceValidator validator;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        validator = new UserExistenceValidator(userRepository);
    }

    @Test
    void givenExistingUserId_whenValidate_thenNoViolations() {
        UUID id = UUID.randomUUID();
        when(userRepository.existsById(id)).thenReturn(true);

        Set<SimpleViolation> violations = validator.validate(id);

        assertTrue(violations.isEmpty());
    }

    @Test
    void givenNonExistingUserId_whenValidate_thenReturnsViolation() {
        UUID id = UUID.randomUUID();
        when(userRepository.existsById(id)).thenReturn(false);

        Set<SimpleViolation> violations = validator.validate(id);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());

        SimpleViolation violation = null;
        for (SimpleViolation v : violations) {
            violation = v;
            break;
        }

        assertEquals("user.id", violation.getViolator());
        assertTrue(violation.getViolation().contains("does not exist"));
    }
}
