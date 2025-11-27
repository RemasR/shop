package com.example.usecase.user;

import com.example.shop.domain.dto.SimpleViolation;
import com.example.shop.domain.dto.UserDTO;
import com.example.shop.domain.model.User;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.usecase.ValidationException;
import com.example.shop.domain.usecase.ValidationExecutor;
import com.example.shop.domain.usecase.user.UpdateUserUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UpdateUserUsecaseTest {

    private UserRepository userRepository;
    private ValidationExecutor<String> existenceValidator;
    private ValidationExecutor<User> userValidator;
    private UpdateUserUsecase updateUserUsecase;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        existenceValidator = mock(ValidationExecutor.class);
        userValidator = mock(ValidationExecutor.class);
        updateUserUsecase = new UpdateUserUsecase(userRepository, existenceValidator, userValidator);
    }

    @Test
    void givenExistingUser_whenUpdateName_thenNameIsUpdated() {
        String userId = UUID.randomUUID().toString();
        User existingUser = new User(userId, "OldName", "email@test.com", "+962791234567");

        when(existenceValidator.validateAndThrow(userId)).thenReturn(Set.of());
        when(userValidator.validateAndThrow(any(User.class))).thenReturn(Set.of());
        when(userRepository.findById(userId)).thenReturn(existingUser);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserDTO dto = new UserDTO("NewName", null, null);
        User result = updateUserUsecase.execute(userId, dto);

        assertEquals("NewName", result.getName());
        assertEquals("email@test.com", result.getEmail());
        assertEquals("+962791234567", result.getPhoneNumber());

        verify(existenceValidator, times(1)).validateAndThrow(userId);
        verify(userValidator, times(1)).validateAndThrow(existingUser);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void givenExistingUser_whenUpdateEmail_thenEmailIsUpdated() {
        String userId = UUID.randomUUID().toString();
        User existingUser = new User(userId, "Khalid", "old@test.com", "+962791234567");

        when(existenceValidator.validateAndThrow(userId)).thenReturn(Set.of());
        when(userValidator.validateAndThrow(any(User.class))).thenReturn(Set.of());
        when(userRepository.findById(userId)).thenReturn(existingUser);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserDTO dto = new UserDTO(null, "new@test.com", null);
        User result = updateUserUsecase.execute(userId, dto);

        assertEquals("Khalid", result.getName());
        assertEquals("new@test.com", result.getEmail());
        assertEquals("+962791234567", result.getPhoneNumber());

        verify(existenceValidator, times(1)).validateAndThrow(userId);
        verify(userValidator, times(1)).validateAndThrow(existingUser);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void givenExistingUser_whenUpdatePhoneNumber_thenPhoneIsUpdated() {
        String userId = UUID.randomUUID().toString();
        User existingUser = new User(userId, "Khalid", "email@test.com", "+962791111111");

        when(existenceValidator.validateAndThrow(userId)).thenReturn(Set.of());
        when(userValidator.validateAndThrow(any(User.class))).thenReturn(Set.of());
        when(userRepository.findById(userId)).thenReturn(existingUser);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserDTO dto = new UserDTO(null, null, "+962792222222");
        User result = updateUserUsecase.execute(userId, dto);

        assertEquals("Khalid", result.getName());
        assertEquals("email@test.com", result.getEmail());
        assertEquals("+962792222222", result.getPhoneNumber());

        verify(existenceValidator, times(1)).validateAndThrow(userId);
        verify(userValidator, times(1)).validateAndThrow(existingUser);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void givenNonExistingUser_whenUpdate_thenThrowsValidationException() {
        String userId = UUID.randomUUID().toString();
        UserDTO dto = new UserDTO("NewName", null, null);

        when(existenceValidator.validateAndThrow(userId))
                .thenThrow(new ValidationException(Set.of(
                        new SimpleViolation("user.id", "User with ID " + userId + " does not exist")
                )));

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> updateUserUsecase.execute(userId, dto)
        );

        String message = exception.getMessage();
        assertTrue(message.contains("user.id"));
        assertTrue(message.contains("does not exist"));

        verify(existenceValidator, times(1)).validateAndThrow(userId);
        verify(userRepository, never()).findById(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void givenInvalidUpdatedUser_whenUpdate_thenThrowsValidationException() {
        String userId = UUID.randomUUID().toString();
        User existingUser = new User(userId, "OldName", "old@test.com", "+962791234567");
        UserDTO dto = new UserDTO("ab", "invalid-email", null);

        when(existenceValidator.validateAndThrow(userId)).thenReturn(Set.of());
        when(userRepository.findById(userId)).thenReturn(existingUser);
        when(userValidator.validateAndThrow(any(User.class)))
                .thenThrow(new ValidationException(Set.of(
                        new SimpleViolation("user.name", "Name must be at least 3 characters"),
                        new SimpleViolation("user.email", "Invalid email format")
                )));

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> updateUserUsecase.execute(userId, dto)
        );

        String message = exception.getMessage();
        assertTrue(message.contains("user.name"));
        assertTrue(message.contains("user.email"));

        verify(existenceValidator, times(1)).validateAndThrow(userId);
        verify(userValidator, times(1)).validateAndThrow(existingUser);
        verify(userRepository, never()).save(any());
    }

    @Test
    void givenExistingUser_whenUpdateAllFields_thenAllFieldsAreUpdated() {
        String userId = UUID.randomUUID().toString();
        User existingUser = new User(userId, "OldName", "old@test.com", "+962791111111");

        when(existenceValidator.validateAndThrow(userId)).thenReturn(Set.of());
        when(userValidator.validateAndThrow(any(User.class))).thenReturn(Set.of());
        when(userRepository.findById(userId)).thenReturn(existingUser);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserDTO dto = new UserDTO("NewName", "new@test.com", "+962792222222");
        User result = updateUserUsecase.execute(userId, dto);

        assertEquals("NewName", result.getName());
        assertEquals("new@test.com", result.getEmail());
        assertEquals("+962792222222", result.getPhoneNumber());

        verify(existenceValidator, times(1)).validateAndThrow(userId);
        verify(userValidator, times(1)).validateAndThrow(existingUser);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
    }
}