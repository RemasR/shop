package com.example.user;

import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.usecase.ValidationExecutor;
import com.example.shop.domain.usecase.ValidationException;
import com.example.shop.domain.usecase.user.FindUserByIdUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class FindUserByIdUsecaseTest {

    private UserRepository userRepository;
    private ValidationExecutor<UUID> validationExecutor;
    private FindUserByIdUsecase findUserByIdUsecase;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        validationExecutor = mock(ValidationExecutor.class);
        findUserByIdUsecase = new FindUserByIdUsecase(userRepository, validationExecutor);
    }

    @Test
    void givenValidUserId_whenFindById_thenReturnsUser() {
        UUID userId = UUID.randomUUID();
        User expectedUser = new User(userId, "Khalid", "khalid@test.com", "+962794128940");

        // Validator passes
        when(validationExecutor.validateAndThrow(userId)).thenReturn(Set.of());
        when(userRepository.findById(userId)).thenReturn(expectedUser);

        User result = findUserByIdUsecase.execute(userId);

        assertNotNull(result);
        assertEquals(expectedUser.getId(), result.getId());
        assertEquals(expectedUser.getName(), result.getName());
        assertEquals(expectedUser.getEmail(), result.getEmail());
        assertEquals(expectedUser.getPhoneNumber(), result.getPhoneNumber());

        verify(validationExecutor, times(1)).validateAndThrow(userId);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void givenInvalidUserId_whenFindById_thenThrowsValidationException() {
        UUID userId = UUID.randomUUID();

        doThrow(new ValidationException(Set.of())).when(validationExecutor).validateAndThrow(userId);

        assertThrows(ValidationException.class, () -> findUserByIdUsecase.execute(userId));

        verify(validationExecutor, times(1)).validateAndThrow(userId);
        verify(userRepository, never()).findById(any());
    }

    @Test
    void givenNullUserId_whenFindById_thenThrowsValidationException() {
        doThrow(new ValidationException(Set.of())).when(validationExecutor).validateAndThrow(null);

        assertThrows(ValidationException.class, () -> findUserByIdUsecase.execute(null));

        verify(validationExecutor, times(1)).validateAndThrow(null);
        verify(userRepository, never()).findById(any());
    }
}
