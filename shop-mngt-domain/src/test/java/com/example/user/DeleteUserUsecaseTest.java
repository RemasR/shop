package com.example.user;

import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.usecase.user.DeleteUserUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class DeleteUserUsecaseTest {

    private UserRepository userRepository;
    private DeleteUserUsecase deleteUserUsecase;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        deleteUserUsecase = new DeleteUserUsecase(userRepository);
    }

    @Test
    void givenExistingUserId_whenDeleteUser_thenRepositoryCalled() {
        UUID id = UUID.randomUUID();

        when(userRepository.existsById(id)).thenReturn(true);

        deleteUserUsecase.execute(id);

        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    void givenNonExistingUserId_whenDeleteUser_thenThrowsException() {
        UUID id = UUID.randomUUID();

        when(userRepository.existsById(id)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> deleteUserUsecase.execute(id));

        verify(userRepository, never()).deleteById(id);
    }

    @Test
    void givenNullId_whenDeleteUser_thenThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> deleteUserUsecase.execute(null));
        verify(userRepository, never()).deleteById(any());
    }
}
