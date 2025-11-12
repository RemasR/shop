package com.example.user;

import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.usecase.ValidationException;
import com.example.shop.domain.usecase.user.FindUserByIdUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FindUserByIdUsecaseTest {

    private UserRepository userRepository;
    private FindUserByIdUsecase findUserByIdUsecase;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        findUserByIdUsecase = new FindUserByIdUsecase(userRepository);
    }

    @Test
    void givenExistingUserId_whenFindById_thenReturnsUser() {
        UUID userId = UUID.randomUUID();
        User expectedUser = new User(userId, "Khalid", "khalid@test.com", "+962794128940");

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(expectedUser);

        User result = findUserByIdUsecase.execute(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("Khalid", result.getName());
        assertEquals("khalid@test.com", result.getEmail());
        assertEquals("+962794128940", result.getPhoneNumber());

        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void givenNonExistingUserId_whenFindById_thenThrowsValidationException() {
        UUID userId = UUID.randomUUID();

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(
                ValidationException.class,
                () -> findUserByIdUsecase.execute(userId)
        );

        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, never()).findById(any());
    }

    @Test
    void givenNullUserId_whenFindById_thenThrowsValidationException() {
        assertThrows(
                ValidationException.class,
                () -> findUserByIdUsecase.execute(null)
        );

        verify(userRepository, never()).findById(any());
    }
}
