package com.example.shop.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class User {

    private final UUID id;
    private String name;
    private String email;
    private String phoneNumber;
}