// CreateUserUsecaseTest.java
package com.example.user;

import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.usecase.user.CreateUserUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CreateUserUsecaseTest {

    private UserRepository userRepository;
    private CreateUserUsecase createUserUsecase;

    @BeforeEach
    void setUp() {
        // Create mock repository
        userRepository = mock(UserRepository.class);
        // Inject mock into use case
        createUserUsecase = new CreateUserUsecase(userRepository);
    }

    @Test
    void givenValidData_whenCreateUser_thenUserIsCreated() {
        String name = "Khalid";
        String email = "khalid@gmail.com";
        String phoneNumber = "+962794128940";

        when(userRepository.findByEmail(email)).thenReturn(null);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            return invocation.getArgument(0);
        });

        User result = createUserUsecase.execute(name, email, phoneNumber);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(name, result.getName());
        assertEquals(email, result.getEmail());
        assertEquals(phoneNumber, result.getPhoneNumber());

        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void givenShortName_whenCreateUser_thenThrowsException() {
        String shortName = "Ab";
        String email = "khalid@gmail.com";
        String phoneNumber = "+962794128940";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> createUserUsecase.execute(shortName, email, phoneNumber)
        );

        assertTrue(exception.getMessage().contains("Name"));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void givenInvalidEmail_whenCreateUser_thenThrowsException() {
        String name = "Khalid";
        String invalidEmail = "not-an-email";
        String phoneNumber = "+962794128940";

        assertThrows(
                IllegalArgumentException.class,
                () -> createUserUsecase.execute(name, invalidEmail, phoneNumber)
        );

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void givenInvalidPhoneNumber_whenCreateUser_thenThrowsException() {
        String name = "Khalid";
        String email = "khalid@gmail.com";
        String invalidPhone = "1234567890";

        assertThrows(
                IllegalArgumentException.class,
                () -> createUserUsecase.execute(name, email, invalidPhone)
        );

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void givenExistingEmail_whenCreateUser_thenThrowsException() {
        String name = "Khalid";
        String email = "existing@gmail.com";
        String phoneNumber = "+962794128940";

        User existingUser = new User(UUID.randomUUID(), "Other", email, "+962791234567");
        when(userRepository.findByEmail(email)).thenReturn(existingUser);

        assertThrows(
                IllegalStateException.class,
                () -> createUserUsecase.execute(name, email, phoneNumber)
        );

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void givenNullName_whenCreateUser_thenThrowsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> createUserUsecase.execute(null, "email@test.com", "+962794128940")
        );
    }
}