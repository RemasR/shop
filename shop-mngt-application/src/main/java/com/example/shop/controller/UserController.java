package com.example.shop.controller;

import com.example.shop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private UserService userService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }
}
/*
POST   /api/users              - Create user
GET    /api/users/{id}         - Get user by ID
GET    /api/users              - Get all users
PUT    /api/users/{id}         - Update user
DELETE /api/users/{id}         - Delete user
 */