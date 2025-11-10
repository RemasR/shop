package com.example.shop.domain.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String name;
    private String email;
    private String phoneNumber;

    public UserDTO(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

}
