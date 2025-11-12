package com.example.user;

import com.example.shop.domain.dto.UserDTO;
import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.usecase.ValidationException;
import com.example.shop.domain.usecase.user.CreateUserUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void givenValidUser_whenExecute_thenUserIsCreatedAndSaved() {
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
    void givenInvalidUser_whenExecute_thenThrowsValidationExceptionAndDoesNotSave() {
        UserDTO dto = UserDTO.builder()
                .name("ab")
                .email("not-an-email")
                .phoneNumber("1234567890")
                .build();

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> createUserUsecase.execute(dto)
        );

        assertNotNull(exception.getViolations());
        assertFalse(exception.getViolations().isEmpty());

        verify(userRepository, never()).save(any(User.class));
    }
}
