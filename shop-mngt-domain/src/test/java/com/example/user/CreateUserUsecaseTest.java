package com.example.user;

import com.example.shop.domain.dto.SimpleViolation;
import com.example.shop.domain.dto.UserDTO;
import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.usecase.ValidationException;
import com.example.shop.domain.usecase.ValidationExecutor;
import com.example.shop.domain.usecase.user.CreateUserUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import java.util.Set;


public class CreateUserUsecaseTest {

    private UserRepository userRepository;
    private ValidationExecutor<UserDTO> validationExecutor;
    private CreateUserUsecase createUserUsecase;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        validationExecutor = mock(ValidationExecutor.class);
        createUserUsecase = new CreateUserUsecase(userRepository, validationExecutor);
    }

    @Test
    void givenValidUser_whenExecute_thenUserIsCreatedAndSaved() {
        UserDTO dto = UserDTO.builder()
                .name("Khalid")
                .email("khalid@gmail.com")
                .phoneNumber("+962794128940")
                .build();

        when(validationExecutor.validateAndThrow(dto)).thenReturn(Set.of());

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = createUserUsecase.execute(dto);

        assertNotNull(result);
        assertEquals("Khalid", result.getName());

        verify(validationExecutor, times(1)).validateAndThrow(dto);
        verify(userRepository, times(1)).save(any(User.class));
    }


    @Test
    void givenInvalidUser_whenExecute_thenThrowsValidationExceptionWithMessage() {
        UserDTO dto = UserDTO.builder()
                .name("ab")
                .email("not-an-email")
                .phoneNumber("1234567890")
                .build();

        Set<SimpleViolation> violations = Set.of(
                new SimpleViolation("name", "too short"),
                new SimpleViolation("email", "invalid")
        );
        doThrow(new ValidationException(violations)).when(validationExecutor).validateAndThrow(dto);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> createUserUsecase.execute(dto)
        );

        String message = exception.getMessage();
        assertTrue(message.contains("name"));
        assertTrue(message.contains("email"));
        assertTrue(message.contains("too short"));
        assertTrue(message.contains("invalid"));

        verify(userRepository, never()).save(any(User.class));
    }

}
