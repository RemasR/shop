package com.example.shop.domain.entity;

import lombok.Data;

import java.util.UUID;

@Data
public class User {

    private final UUID id;
    private String name;
    private String email;
    private String phoneNumber;

    public User(UUID id, String name, String email, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}