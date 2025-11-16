package com.example.usecase.user;

import com.example.shop.domain.usecase.ValidationExecutor;
import com.example.shop.domain.usecase.ValidationException;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.usecase.user.DeleteUserUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class DeleteUserUsecaseTest {

    private UserRepository userRepository;
    private ValidationExecutor<String> validationExecutor;
    private DeleteUserUsecase deleteUserUsecase;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        validationExecutor = mock(ValidationExecutor.class);
        deleteUserUsecase = new DeleteUserUsecase(userRepository, validationExecutor);
    }

    @Test
    void givenValidId_whenExecute_thenDeletesUser() {
        String userId = UUID.randomUUID().toString();

        when(validationExecutor.validateAndThrow(userId)).thenReturn(Set.of());

        deleteUserUsecase.execute(userId);

        verify(validationExecutor, times(1)).validateAndThrow(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void givenInvalidId_whenExecute_thenThrowsValidationException() {
        String userId = UUID.randomUUID().toString();

        when(validationExecutor.validateAndThrow(userId))
                .thenThrow(new ValidationException(Set.of()));

        assertThrows(ValidationException.class, () -> deleteUserUsecase.execute(userId));

        verify(validationExecutor, times(1)).validateAndThrow(userId);
        verify(userRepository, never()).deleteById(any());
    }
}
