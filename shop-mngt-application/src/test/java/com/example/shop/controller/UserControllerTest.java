package com.example.shop.controller;

import com.example.shop.domain.dto.UserDTO;
import com.example.shop.domain.model.User;
import com.example.shop.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void givenValidUserDTO_whenCreateUser_thenReturns200AndUser() throws Exception {
        UserDTO dto = UserDTO.builder()
                .name("Ahmad")
                .email("ahmad@test.com")
                .phoneNumber("+962791234567")
                .build();

        User createdUser = User.builder()
                .id(UUID.randomUUID().toString())
                .name("Ahmad")
                .email("ahmad@test.com")
                .phoneNumber("+962791234567")
                .build();

        when(userService.registerUser(any(UserDTO.class))).thenReturn(createdUser);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Ahmad")))
                .andExpect(jsonPath("$.email", is("ahmad@test.com")))
                .andExpect(jsonPath("$.phoneNumber", is("+962791234567")))
                .andExpect(jsonPath("$.id", notNullValue()));

        verify(userService, times(1)).registerUser(any(UserDTO.class));
    }

    @Test
    void givenExistingUserId_whenGetUserById_thenReturns200AndUser() throws Exception {
        String userId = UUID.randomUUID().toString();
        User user = User.builder()
                .id(userId)
                .name("Sara")
                .email("sara@test.com")
                .phoneNumber("+962791234567")
                .build();

        when(userService.getUserById(userId)).thenReturn(user);

        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userId)))
                .andExpect(jsonPath("$.name", is("Sara")))
                .andExpect(jsonPath("$.email", is("sara@test.com")))
                .andExpect(jsonPath("$.phoneNumber", is("+962791234567")));

        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void givenExistingUser_whenUpdateUser_thenReturns200AndUpdatedUser() throws Exception {
        String userId = UUID.randomUUID().toString();
        UserDTO updateDTO = UserDTO.builder()
                .name("NewName")
                .build();

        User updatedUser = User.builder()
                .id(userId)
                .name("NewName")
                .email("old@test.com")
                .phoneNumber("+962791234567")
                .build();

        when(userService.updateUser(eq(userId), any(UserDTO.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userId)))
                .andExpect(jsonPath("$.name", is("NewName")))
                .andExpect(jsonPath("$.email", is("old@test.com")))
                .andExpect(jsonPath("$.phoneNumber", is("+962791234567")));

        verify(userService, times(1)).updateUser(eq(userId), any(UserDTO.class));
    }

    @Test
    void givenExistingUser_whenDeleteUser_thenReturns200() throws Exception {
        String userId = UUID.randomUUID().toString();

        doNothing().when(userService).deleteUser(userId);

        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    void givenValidUsers_whenGetAllUsers_thenReturns200AndUserList() throws Exception {
        User user1 = User.builder()
                .id(UUID.randomUUID().toString())
                .name("User1")
                .email("user1@test.com")
                .phoneNumber("+962791234567")
                .build();

        User user2 = User.builder()
                .id(UUID.randomUUID().toString())
                .name("User2")
                .email("user2@test.com")
                .phoneNumber("+962791234568")
                .build();

        List<User> users = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("User1")))
                .andExpect(jsonPath("$[1].name", is("User2")));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void givenNoUsers_whenGetAllUsers_thenReturns200AndEmptyList() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(userService, times(1)).getAllUsers();
    }
}