package com.example.usecase.user;

import com.example.shop.domain.dto.SimpleViolation;
import com.example.shop.domain.dto.UserDTO;
import com.example.shop.domain.model.User;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.usecase.ValidationException;
import com.example.shop.domain.usecase.ValidationExecutor;
import com.example.shop.domain.usecase.user.CreateUserUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CreateUserUsecaseTest {

    private UserRepository userRepository;
    private ValidationExecutor<User> validationExecutor;
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

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User result = createUserUsecase.execute(dto);

        assertNotNull(result);
        assertEquals("Khalid", result.getName());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(validationExecutor, times(1)).validateAndThrow(userCaptor.capture());
        verify(userRepository, times(1)).save(any(User.class));

        User validatedUser = userCaptor.getValue();

        assertEquals("Khalid", validatedUser.getName());
        assertEquals("khalid@gmail.com", validatedUser.getEmail());
        assertEquals("+962794128940", validatedUser.getPhoneNumber());
    }



    @Test
    void givenInvalidUser_whenExecute_thenThrowsValidationExceptionWithMessage() {
        UserDTO dto = UserDTO.builder()
                .name("ab")
                .email("not-an-email")
                .phoneNumber("1234567890")
                .build();

        Set<SimpleViolation> violations = Set.of(
                new SimpleViolation("user.name", "Name must be at least 3 characters"),
                new SimpleViolation("user.email", "Email format is invalid")
        );

        doThrow(new ValidationException(violations))
                .when(validationExecutor).validateAndThrow(any(User.class));

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> createUserUsecase.execute(dto)
        );

        String message = exception.getMessage();
        assertTrue(message.contains("user.name"));
        assertTrue(message.contains("user.email"));
        assertTrue(message.contains("Name must be at least 3 characters"));
        assertTrue(message.contains("Email format is invalid"));

        verify(userRepository, never()).save(any(User.class));
    }

/*
 for(SimpleViolation vicontainsolation : violations){ // TODO: FIX ME
            assert(violations_2.(violation));
        }
 */
}
