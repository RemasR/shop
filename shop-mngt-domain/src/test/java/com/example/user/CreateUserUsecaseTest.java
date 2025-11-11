package com.example.user;

import com.example.shop.domain.dto.UserDTO;
import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.usecase.ValidationExecutor;
import com.example.shop.domain.usecase.user.CreateUserUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CreateUserUsecaseTest {

    private UserRepository userRepository;
    private CreateUserUsecase createUserUsecase;
    private ValidationExecutor validationExecutor;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        validationExecutor = mock(ValidationExecutor.class);
        createUserUsecase = new CreateUserUsecase(userRepository, validationExecutor);
    }

    @Test
    void givenValidDTO_whenCreateUser_thenUserIsCreated() {
        UserDTO dto = UserDTO.builder()
                .name("Khalid")
                .email("khalid@gmail.com")
                .phoneNumber("+962794128940")
                .build();

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = createUserUsecase.execute(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Khalid", result.getName());
        assertEquals("khalid@gmail.com", result.getEmail());
        assertEquals("+962794128940", result.getPhoneNumber());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void givenShortName_whenCreateUser_thenThrowsException() {
        UserDTO dto = UserDTO.builder()
                .name("ab")
                .email("khalid@gmail.com")
                .phoneNumber("+962794128940")
                .build();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> createUserUsecase.execute(dto)
        );

        assertTrue(exception.getMessage().contains("Name"));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void givenInvalidEmail_whenCreateUser_thenThrowsException() {
        UserDTO dto = UserDTO.builder()
                .name("Khalid")
                .email("not-an-email")
                .phoneNumber("+962794128940")
                .build();

        assertThrows(
                IllegalArgumentException.class,
                () -> createUserUsecase.execute(dto)
        );

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void givenInvalidPhoneNumber_whenCreateUser_thenThrowsException() {
        UserDTO dto = UserDTO.builder()
                .name("Khalid")
                .email("not-an-email")
                .phoneNumber("1234567890")
                .build();

        assertThrows(
                IllegalArgumentException.class,
                () -> createUserUsecase.execute(dto)
        );

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void givenNullName_whenCreateUser_thenThrowsException() {
        UserDTO dto = UserDTO.builder()
                .name(null)
                .email("email@test.com")
                .phoneNumber("+962794128940")
                .build();

        assertThrows(
                IllegalArgumentException.class,
                () -> createUserUsecase.execute(dto)
        );

        verify(userRepository, never()).save(any(User.class));
    }
}