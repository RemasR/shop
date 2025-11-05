package com.example.shop.domain.entity;

import java.util.UUID;

public class User {

    private final UUID id;
    private String name;
    private String email;
    private String phoneNumber;

    public User(UUID id, String name, String email, String phoneNumber) {
        validateName(name);
        validateEmail(email);
        validatePhoneNumber(phoneNumber);

        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setName(String name) {
        validateName(name);
        this.name = name;
    }

    public void setEmail(String email) {
        validateEmail(email);
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        validatePhoneNumber(phoneNumber);
        this.phoneNumber = phoneNumber;
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty() || name.length() < 3) {
            throw new IllegalArgumentException("Name must be at least 3 characters");
        }
    }

    private void validateEmail(String email) {
        if (email == null) throw new IllegalArgumentException("Email is null");
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!email.matches(emailRegex)) throw new IllegalArgumentException("Invalid email");
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null) throw new IllegalArgumentException("Phone number is null");
        String phoneRegex = "^\\+9627[7-9]\\d{7}$";
        if (!phoneNumber.matches(phoneRegex)) throw new IllegalArgumentException("Invalid phone number");
    }
}