package com.example.user;

import com.example.shop.domain.dto.UserDTO;
import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.usecase.user.UpdateUserUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UpdateUserUsecaseTest {

    private UserRepository userRepository;
    private UpdateUserUsecase updateUserUsecase;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        updateUserUsecase = new UpdateUserUsecase(userRepository);
    }

    @Test
    void givenExistingUser_whenUpdateName_thenNameIsUpdated() {
        UUID userId = UUID.randomUUID();
        User existingUser = new User(userId, "OldName", "email@test.com", "+962791234567");

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(existingUser);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserDTO dto = new UserDTO("NewName", null, null);
        User result = updateUserUsecase.execute(userId, dto);

        assertEquals("NewName", result.getName());
        assertEquals("email@test.com", result.getEmail());
        assertEquals("+962791234567", result.getPhoneNumber());

        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void givenExistingUser_whenUpdateEmail_thenEmailIsUpdated() {
        UUID userId = UUID.randomUUID();
        User existingUser = new User(userId, "Khalid", "old@test.com", "+962791234567");

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(existingUser);
        when(userRepository.findByEmail("new@test.com")).thenReturn(null);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserDTO dto = new UserDTO(null, "new@test.com", null);
        User result = updateUserUsecase.execute(userId, dto);

        assertEquals("Khalid", result.getName());
        assertEquals("new@test.com", result.getEmail());
        assertEquals("+962791234567", result.getPhoneNumber());

        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).findByEmail("new@test.com");
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void givenExistingUser_whenUpdatePhoneNumber_thenPhoneIsUpdated() {
        UUID userId = UUID.randomUUID();
        User existingUser = new User(userId, "Khalid", "email@test.com", "+962791111111");

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(existingUser);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserDTO dto = new UserDTO(null, null, "+962792222222");
        User result = updateUserUsecase.execute(userId, dto);

        assertEquals("Khalid", result.getName());
        assertEquals("email@test.com", result.getEmail());
        assertEquals("+962792222222", result.getPhoneNumber());

        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void givenNonExistentUser_whenUpdate_thenThrowsException() {
        UUID userId = UUID.randomUUID();

        when(userRepository.existsById(userId)).thenReturn(false);

        UserDTO dto = new UserDTO("NewName", null, null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> updateUserUsecase.execute(userId, dto)
        );

        assertTrue(exception.getMessage().contains("does not exist"));
        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, never()).findById(any());
        verify(userRepository, never()).save(any());
    }
}
