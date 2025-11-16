package com.example.usecase.user;

import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.usecase.user.ListAllUserUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ListAllUserUsecaseTest {

    private UserRepository userRepository;
    private ListAllUserUsecase listAllUserUsecase;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        listAllUserUsecase = new ListAllUserUsecase(userRepository);
    }

    @Test
    void givenValidUsers_whenListAllUsers_thenReturnsAllUsers() {
        User user1 = new User(UUID.randomUUID(), "Alice", "alice@test.com", "+962791111111");
        User user2 = new User(UUID.randomUUID(), "Bob", "bob@test.com", "+962792222222");
        User user3 = new User(UUID.randomUUID(), "Charlie", "charlie@test.com", "+962793333333");

        List<User> expectedUsers = Arrays.asList(user1, user2, user3);

        when(userRepository.findAllUsers()).thenReturn(expectedUsers);

        List<User> result = listAllUserUsecase.execute();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains(user1));
        assertTrue(result.contains(user2));
        assertTrue(result.contains(user3));

        verify(userRepository, times(1)).findAllUsers();
    }

    @Test
    void givenEmptyList_whenListAllUsers_thenReturnsEmptyList() {
        when(userRepository.findAllUsers()).thenReturn(Collections.emptyList());

        List<User> result = listAllUserUsecase.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(userRepository, times(1)).findAllUsers();
    }

    @Test
    void givenListWithSingleUser_whenListAllUsers_thenReturnsSingletonList() {
        User user = new User(UUID.randomUUID(), "John", "john@test.com", "+962794444444");
        when(userRepository.findAllUsers()).thenReturn(Collections.singletonList(user));

        List<User> result = listAllUserUsecase.execute();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(user, result.get(0));
        assertEquals("John", result.get(0).getName());
        assertEquals("john@test.com", result.get(0).getEmail());

        verify(userRepository, times(1)).findAllUsers();
    }
}
