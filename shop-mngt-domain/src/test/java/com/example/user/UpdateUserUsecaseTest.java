// test/UpdateUserUsecaseTest.java
package com.example.user;

import com.example.shop.domain.dto.UpdateUserDTO;
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

        when(userRepository.findById(userId)).thenReturn(existingUser);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UpdateUserDTO dto = new UpdateUserDTO("NewName", null, null);
        User result = updateUserUsecase.execute(userId, dto);

        assertEquals("NewName", result.getName());
        assertEquals("email@test.com", result.getEmail());
        assertEquals("+962791234567", result.getPhoneNumber());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void givenExistingUser_whenUpdateEmail_thenEmailIsUpdated() {
        UUID userId = UUID.randomUUID();
        User existingUser = new User(userId, "Khalid", "old@test.com", "+962791234567");

        when(userRepository.findById(userId)).thenReturn(existingUser);
        when(userRepository.findByEmail("new@test.com")).thenReturn(null);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UpdateUserDTO dto = new UpdateUserDTO(null, "new@test.com", null);
        User result = updateUserUsecase.execute(userId, dto);

        assertEquals("Khalid", result.getName());
        assertEquals("new@test.com", result.getEmail());
        assertEquals("+962791234567", result.getPhoneNumber());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).findByEmail("new@test.com");
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void givenExistingUser_whenUpdatePhoneNumber_thenPhoneIsUpdated() {
        UUID userId = UUID.randomUUID();
        User existingUser = new User(userId, "Khalid", "email@test.com", "+962791111111");

        when(userRepository.findById(userId)).thenReturn(existingUser);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UpdateUserDTO dto = new UpdateUserDTO(null, null, "+962792222222");
        User result = updateUserUsecase.execute(userId, dto);

        assertEquals("Khalid", result.getName());
        assertEquals("email@test.com", result.getEmail());
        assertEquals("+962792222222", result.getPhoneNumber());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void givenNonExistentUser_whenUpdate_thenThrowsException() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(null);

        UpdateUserDTO dto = new UpdateUserDTO("NewName", null, null);

        assertThrows(
                IllegalArgumentException.class,
                () -> updateUserUsecase.execute(userId, dto)
        );

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any());
    }

    @Test
    void givenEmailTakenByAnotherUser_whenUpdateEmail_thenThrowsException() {
        UUID userId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();

        User existingUser = new User(userId, "Khalid", "khalid@test.com", "+962791234567");
        User otherUser = new User(otherUserId, "Other", "taken@test.com", "+962799999999");

        when(userRepository.findById(userId)).thenReturn(existingUser);
        when(userRepository.findByEmail("taken@test.com")).thenReturn(otherUser);

        UpdateUserDTO dto = new UpdateUserDTO(null, "taken@test.com", null);

        assertThrows(
                IllegalStateException.class,
                () -> updateUserUsecase.execute(userId, dto)
        );

        verify(userRepository, never()).save(any());
    }

    @Test
    void givenInvalidName_whenUpdate_thenThrowsException() {
        UUID userId = UUID.randomUUID();
        User existingUser = new User(userId, "Khalid", "email@test.com", "+962791234567");

        when(userRepository.findById(userId)).thenReturn(existingUser);

        UpdateUserDTO dto = new UpdateUserDTO("Ab", null, null);

        assertThrows(
                IllegalArgumentException.class,
                () -> updateUserUsecase.execute(userId, dto)
        );

        verify(userRepository, never()).save(any());
    }

    @Test
    void givenEmptyName_whenUpdate_thenThrowsException() {
        UUID userId = UUID.randomUUID();
        User existingUser = new User(userId, "Khalid", "email@test.com", "+962791234567");

        when(userRepository.findById(userId)).thenReturn(existingUser);

        UpdateUserDTO dto = new UpdateUserDTO("   ", null, null);

        assertThrows(
                IllegalArgumentException.class,
                () -> updateUserUsecase.execute(userId, dto)
        );

        verify(userRepository, never()).save(any());
    }

    @Test
    void givenInvalidEmail_whenUpdate_thenThrowsException() {
        UUID userId = UUID.randomUUID();
        User existingUser = new User(userId, "Khalid", "old@test.com", "+962791234567");

        when(userRepository.findById(userId)).thenReturn(existingUser);

        UpdateUserDTO dto = new UpdateUserDTO(null, "not-an-email", null);

        assertThrows(
                IllegalArgumentException.class,
                () -> updateUserUsecase.execute(userId, dto)
        );

        verify(userRepository, never()).save(any());
    }

    @Test
    void givenEmptyEmail_whenUpdate_thenThrowsException() {
        UUID userId = UUID.randomUUID();
        User existingUser = new User(userId, "Khalid", "old@test.com", "+962791234567");

        when(userRepository.findById(userId)).thenReturn(existingUser);

        UpdateUserDTO dto = new UpdateUserDTO(null, "   ", null);

        assertThrows(
                IllegalArgumentException.class,
                () -> updateUserUsecase.execute(userId, dto)
        );

        verify(userRepository, never()).save(any());
    }

    @Test
    void givenInvalidPhoneNumber_whenUpdate_thenThrowsException() {
        UUID userId = UUID.randomUUID();
        User existingUser = new User(userId, "Khalid", "email@test.com", "+962791234567");

        when(userRepository.findById(userId)).thenReturn(existingUser);

        UpdateUserDTO dto = new UpdateUserDTO(null, null, "123456");

        assertThrows(
                IllegalArgumentException.class,
                () -> updateUserUsecase.execute(userId, dto)
        );

        verify(userRepository, never()).save(any());
    }

    @Test
    void givenEmptyPhoneNumber_whenUpdate_thenThrowsException() {
        UUID userId = UUID.randomUUID();
        User existingUser = new User(userId, "Khalid", "email@test.com", "+962791234567");

        when(userRepository.findById(userId)).thenReturn(existingUser);

        UpdateUserDTO dto = new UpdateUserDTO(null, null, "   ");

        assertThrows(
                IllegalArgumentException.class,
                () -> updateUserUsecase.execute(userId, dto)
        );

        verify(userRepository, never()).save(any());
    }

    @Test
    void givenPhoneWithInvalidPrefix_whenUpdate_thenThrowsException() {
        UUID userId = UUID.randomUUID();
        User existingUser = new User(userId, "Khalid", "email@test.com", "+962791234567");

        when(userRepository.findById(userId)).thenReturn(existingUser);

        UpdateUserDTO dto = new UpdateUserDTO(null, null, "+962691234567");

        assertThrows(
                IllegalArgumentException.class,
                () -> updateUserUsecase.execute(userId, dto)
        );

        verify(userRepository, never()).save(any());
    }

    @Test
    void givenMultipleUpdates_whenUpdateSameUser_thenAllUpdatesApplied() {
        UUID userId = UUID.randomUUID();
        User existingUser = new User(userId, "Original", "original@test.com", "+962791111111");

        when(userRepository.findById(userId)).thenReturn(existingUser);
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UpdateUserDTO dto1 = new UpdateUserDTO("Updated1", null, null);
        User result1 = updateUserUsecase.execute(userId, dto1);

        UpdateUserDTO dto2 = new UpdateUserDTO(null, "updated@test.com", null);
        User result2 = updateUserUsecase.execute(userId, dto2);

        UpdateUserDTO dto3 = new UpdateUserDTO(null, null, "+962792222222");
        User result3 = updateUserUsecase.execute(userId, dto3);

        assertEquals("Updated1", result3.getName());
        assertEquals("updated@test.com", result3.getEmail());
        assertEquals("+962792222222", result3.getPhoneNumber());

        verify(userRepository, times(3)).save(existingUser);
    }
}