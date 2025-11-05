package com.example.user;

import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.usecase.user.CreateUserUsecase;
import com.example.shop.dto.UserDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CreateUserUsecaseTest {

    private UserRepository userRepository;
    private CreateUserUsecase createUserUsecase;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        createUserUsecase = new CreateUserUsecase(userRepository);
    }

    @Test
    void givenValidDTO_whenCreateUser_thenUserIsCreated() {
        UserDTO dto = new UserDTO("Khalid", "khalid@gmail.com", "+962794128940");

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = createUserUsecase.execute(dto);

        assertNotNull(result.getId());
        assertEquals("Khalid", result.getName());
        assertEquals("khalid@gmail.com", result.getEmail());
        assertEquals("+962794128940", result.getPhoneNumber());

        verify(userRepository, times(1)).save(result);
    }

    @Test
    void givenInvalidName_whenCreateUser_thenThrowsException() {
        UserDTO dto = new UserDTO("ab", "khalid@gmail.com", "+962794128940");

        assertThrows(IllegalArgumentException.class, () -> createUserUsecase.execute(dto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void givenInvalidEmail_whenCreateUser_thenThrowsException() {
        UserDTO dto = new UserDTO("Khalid", "invalid-email", "+962794128940");

        assertThrows(IllegalArgumentException.class, () -> createUserUsecase.execute(dto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void givenInvalidPhoneNumber_whenCreateUser_thenThrowsException() {
        UserDTO dto = new UserDTO("Khalid", "khalid@gmail.com", "1234567890");

        assertThrows(IllegalArgumentException.class, () -> createUserUsecase.execute(dto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void givenNullDTO_whenCreateUser_thenThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> createUserUsecase.execute(null));
        verify(userRepository, never()).save(any(User.class));
    }
}
