package com.example.user;

import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.usecase.user.FindUserByEmailUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FindUserByEmailUsecaseTest {

    private UserRepository userRepository;
    private FindUserByEmailUsecase findUserByEmailUsecase;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        findUserByEmailUsecase = new FindUserByEmailUsecase(userRepository);
    }

    @Test
    void givenExistingEmail_whenFindByEmail_thenReturnsUser() {
        String email = "khalid@test.com";
        UUID userId = UUID.randomUUID();
        User expectedUser = new User(userId, "Khalid", email, "+962794128940");

        when(userRepository.findByEmail(email)).thenReturn(expectedUser);

        User result = findUserByEmailUsecase.execute(email);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("Khalid", result.getName());
        assertEquals(email, result.getEmail());
        assertEquals("+962794128940", result.getPhoneNumber());

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void givenNonExistingEmail_whenFindByEmail_thenThrowsException() {
        String email = "notfound@test.com";

        when(userRepository.findByEmail(email)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> findUserByEmailUsecase.execute(email)
        );

        assertTrue(exception.getMessage().contains("not found"));
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void givenNullEmail_whenFindByEmail_thenThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> findUserByEmailUsecase.execute(null)
        );

        assertTrue(exception.getMessage().contains("cannot be null"));
        verify(userRepository, never()).findByEmail(any());
    }

    @Test
    void givenEmptyEmail_whenFindByEmail_thenThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> findUserByEmailUsecase.execute("   ")
        );

        assertTrue(exception.getMessage().contains("cannot be null or empty"));
        verify(userRepository, never()).findByEmail(any());
    }

    @Test
    void givenValidEmailWithDifferentCases_whenFindByEmail_thenSearchesAsProvided() {
        String email = "User@Example.COM";
        UUID userId = UUID.randomUUID();
        User expectedUser = new User(userId, "John", email, "+962791234567");

        when(userRepository.findByEmail(email)).thenReturn(expectedUser);

        User result = findUserByEmailUsecase.execute(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void givenEmailWithSpecialCharacters_whenFindByEmail_thenReturnsUser() {
        String email = "user+tag@example.com";
        UUID userId = UUID.randomUUID();
        User expectedUser = new User(userId, "Test User", email, "+962791234567");

        when(userRepository.findByEmail(email)).thenReturn(expectedUser);

        User result = findUserByEmailUsecase.execute(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());

        verify(userRepository, times(1)).findByEmail(email);
    }
}