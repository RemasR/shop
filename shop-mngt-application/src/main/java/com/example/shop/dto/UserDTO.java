package com.example.shop.dto;

public class UserDTO {
    public String name;
    public String email;
    public String phoneNumber;

    public UserDTO(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
}