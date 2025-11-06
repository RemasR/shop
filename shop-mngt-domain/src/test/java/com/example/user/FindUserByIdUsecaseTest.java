package com.example.user;

import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.UserRepository;
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

        when(userRepository.findById(userId)).thenReturn(expectedUser);

        User result = findUserByIdUsecase.execute(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("Khalid", result.getName());
        assertEquals("khalid@test.com", result.getEmail());
        assertEquals("+962794128940", result.getPhoneNumber());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void givenNonExistingUserId_whenFindById_thenThrowsException() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> findUserByIdUsecase.execute(userId)
        );

        assertTrue(exception.getMessage().contains("not found"));
        assertTrue(exception.getMessage().contains(userId.toString()));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void givenNullUserId_whenFindById_thenThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> findUserByIdUsecase.execute(null)
        );

        assertTrue(exception.getMessage().contains("cannot be null"));
        verify(userRepository, never()).findById(any());
    }

    @Test
    void givenMultipleCalls_whenFindById_thenReturnsSameUser() {
        UUID userId = UUID.randomUUID();
        User expectedUser = new User(userId, "Ali", "ali@test.com", "+962791234567");

        when(userRepository.findById(userId)).thenReturn(expectedUser);

        User result1 = findUserByIdUsecase.execute(userId);
        User result2 = findUserByIdUsecase.execute(userId);

        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals(result1.getId(), result2.getId());
        assertEquals(result1.getName(), result2.getName());
        assertEquals(result1.getEmail(), result2.getEmail());
        assertEquals(result1.getPhoneNumber(), result2.getPhoneNumber());

        verify(userRepository, times(2)).findById(userId);
    }

    @Test
    void givenDifferentUserIds_whenFindById_thenReturnsDifferentUsers() {
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();

        User user1 = new User(userId1, "Khalid", "khalid@test.com", "+962791234567");
        User user2 = new User(userId2, "Ahmad", "ahmad@test.com", "+962799999999");

        when(userRepository.findById(userId1)).thenReturn(user1);
        when(userRepository.findById(userId2)).thenReturn(user2);

        User result1 = findUserByIdUsecase.execute(userId1);
        User result2 = findUserByIdUsecase.execute(userId2);

        assertNotEquals(result1.getId(), result2.getId());
        assertNotEquals(result1.getName(), result2.getName());
        assertNotEquals(result1.getEmail(), result2.getEmail());

        verify(userRepository, times(1)).findById(userId1);
        verify(userRepository, times(1)).findById(userId2);
    }

    @Test
    void givenUserWithMinimumValidData_whenFindById_thenReturnsUser() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "Ali", "a@b.c", "+962771234567");

        when(userRepository.findById(userId)).thenReturn(user);

        User result = findUserByIdUsecase.execute(userId);

        assertNotNull(result);
        assertEquals("Ali", result.getName());
        assertEquals("a@b.c", result.getEmail());
        assertEquals("+962771234567", result.getPhoneNumber());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void givenUserWith77PhonePrefix_whenFindById_thenReturnsUser() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "User77", "user@test.com", "+962771234567");

        when(userRepository.findById(userId)).thenReturn(user);

        User result = findUserByIdUsecase.execute(userId);

        assertEquals("+962771234567", result.getPhoneNumber());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void givenUserWith78PhonePrefix_whenFindById_thenReturnsUser() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "User78", "user@test.com", "+962781234567");

        when(userRepository.findById(userId)).thenReturn(user);

        User result = findUserByIdUsecase.execute(userId);

        assertEquals("+962781234567", result.getPhoneNumber());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void givenUserWithValidPhone_whenFindById_thenReturnsUser() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "User79", "user@test.com", "+962791234567");

        when(userRepository.findById(userId)).thenReturn(user);

        User result = findUserByIdUsecase.execute(userId);

        assertEquals("+962791234567", result.getPhoneNumber());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void givenUserWithSpecialEmailCharacters_whenFindById_thenReturnsUser() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "Test User", "test+tag@example.com", "+962791234567");

        when(userRepository.findById(userId)).thenReturn(user);

        User result = findUserByIdUsecase.execute(userId);

        assertEquals("test+tag@example.com", result.getEmail());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void givenRepositoryThrowsException_whenFindById_thenExceptionPropagates() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenThrow(new RuntimeException("Database error"));

        assertThrows(
                RuntimeException.class,
                () -> findUserByIdUsecase.execute(userId)
        );

        verify(userRepository, times(1)).findById(userId);
    }
}