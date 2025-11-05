package com.example;

import com.example.shop.domain.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {
    private User user, invalidUser;
    private UUID id;

    @BeforeEach
    void setUp(){
        id = UUID.randomUUID();
    }

    @Test
    public void givenValidUserData_whenCreateUser_thenUserIsCreated() {
        user = new User(id, "Khalid", "khalid@gmail.com", "+962794128940");

        assertEquals(id, user.getId());

        assertEquals("Khalid", user.getName());
        assertEquals("khalid@gmail.com", user.getEmail());
        assertEquals("+962794128940", user.getPhoneNumber());
    }

    @Test
    public void givenInvalidName_whenCreateUser_thenThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User(id, "ab", "khalid@gmail.com", "+962794128940");
        });
    }

    @Test
    public void givenInvalidEmail_whenCreateUser_thenThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User(id, "Khalid", "invalid-email", "+962794128947");
        });
    }

    @Test
    public void givenInvalidPhoneNumber_whenCreateUser_thenThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User(id, "Khalid", "khalid@gmail.com", "1234567890");
        });
    }

    @Test
    public void givenValidName_whenSetName_thenNameIsUpdated() {
        user = new User(id, "Khalid", "khalid@gmail.com", "+962794128940");

        user.setName("Ahmad");

        assertEquals("Ahmad", user.getName());
    }

    @Test
    public void givenValidEmail_whenSetEmail_thenEmailIsUpdated() {
        user = new User(id, "Khalid", "khalid@gmail.com", "+962794128940");

        user.setEmail("ahmad@gmail.com");

        assertEquals("ahmad@gmail.com", user.getEmail());
    }

    @Test
    public void givenValidPhoneNumber_whenSetPhoneNumber_thenPhoneNumberIsUpdated() {
        user = new User(id, "Khalid", "khalid@gmail.com", "+962794128940");

        user.setPhoneNumber("+962791234565");

        assertEquals("+962791234565", user.getPhoneNumber());
    }

    //need to validated duplicated user but when you learn how to mock your repo
}