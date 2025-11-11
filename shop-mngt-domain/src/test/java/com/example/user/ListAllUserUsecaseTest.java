package com.example.user;

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
    void whenListAllUsers_thenReturnsAllUsers() {
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
    void whenNoUsersExist_thenReturnsEmptyList() {
        when(userRepository.findAllUsers()).thenReturn(Collections.emptyList());

        List<User> result = listAllUserUsecase.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());

        verify(userRepository, times(1)).findAllUsers();
    }

    @Test
    void whenSingleUserExists_thenReturnsSingletonList() {
        User user = new User(UUID.randomUUID(), "John", "john@test.com", "+962794444444");
        List<User> expectedUsers = Collections.singletonList(user);

        when(userRepository.findAllUsers()).thenReturn(expectedUsers);

        List<User> result = listAllUserUsecase.execute();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(user, result.get(0));
        assertEquals("John", result.get(0).getName());
        assertEquals("john@test.com", result.get(0).getEmail());

        verify(userRepository, times(1)).findAllUsers();
    }

    @Test
    void whenListAllUsersCalled_thenReturnsSameUsersEachTime() {
        User user1 = new User(UUID.randomUUID(), "Alice", "alice@test.com", "+962791111111");
        User user2 = new User(UUID.randomUUID(), "Bob", "bob@test.com", "+962792222222");
        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findAllUsers()).thenReturn(users);

        List<User> result1 = listAllUserUsecase.execute();
        List<User> result2 = listAllUserUsecase.execute();

        assertEquals(result1.size(), result2.size());
        assertEquals(result1.get(0).getId(), result2.get(0).getId());
        assertEquals(result1.get(1).getId(), result2.get(1).getId());

        verify(userRepository, times(2)).findAllUsers();
    }

    @Test
    void whenLargeNumberOfUsers_thenReturnsAll() {
        List<User> manyUsers = Arrays.asList(
                new User(UUID.randomUUID(), "User1", "user1@test.com", "+962791111111"),
                new User(UUID.randomUUID(), "User2", "user2@test.com", "+962792222222"),
                new User(UUID.randomUUID(), "User3", "user3@test.com", "+962793333333"),
                new User(UUID.randomUUID(), "User4", "user4@test.com", "+962794444444"),
                new User(UUID.randomUUID(), "User5", "user5@test.com", "+962795555555")
        );

        when(userRepository.findAllUsers()).thenReturn(manyUsers);

        List<User> result = listAllUserUsecase.execute();

        assertNotNull(result);
        assertEquals(5, result.size());

        for (User manyUser : manyUsers) {
            assertTrue(result.contains(manyUser));
        }

        verify(userRepository, times(1)).findAllUsers();
    }

    @Test
    void whenListUsers_thenPreservesUserData() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "Khalid", "khalid@test.com", "+962794128940");
        List<User> users = Collections.singletonList(user);

        when(userRepository.findAllUsers()).thenReturn(users);

        List<User> result = listAllUserUsecase.execute();

        User resultUser = result.get(0);
        assertEquals(userId, resultUser.getId());
        assertEquals("Khalid", resultUser.getName());
        assertEquals("khalid@test.com", resultUser.getEmail());
        assertEquals("+962794128940", resultUser.getPhoneNumber());

        verify(userRepository, times(1)).findAllUsers();
    }

    @Test
    void whenUsersHaveDifferentPhonePrefixes_thenAllReturned() {
        User user77 = new User(UUID.randomUUID(), "User77", "user77@test.com", "+962771234567");
        User user78 = new User(UUID.randomUUID(), "User78", "user78@test.com", "+962781234567");
        User user79 = new User(UUID.randomUUID(), "User79", "user79@test.com", "+962791234567");

        List<User> users = Arrays.asList(user77, user78, user79);

        when(userRepository.findAllUsers()).thenReturn(users);

        List<User> result = listAllUserUsecase.execute();

        assertEquals(3, result.size());
        assertTrue(result.stream().anyMatch(u -> u.getPhoneNumber().startsWith("+96277")));
        assertTrue(result.stream().anyMatch(u -> u.getPhoneNumber().startsWith("+96278")));
        assertTrue(result.stream().anyMatch(u -> u.getPhoneNumber().startsWith("+96279")));

        verify(userRepository, times(1)).findAllUsers();
    }
}