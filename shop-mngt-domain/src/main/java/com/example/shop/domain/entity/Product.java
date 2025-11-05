package com.example.shop.domain.entity;

public class Product {
    private final int id;
    private String name;
    private String description;
    private double price;

    public Product(int id, String name, String description, double price) {
        validateName(name);
        validatePrice(price);

        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        validateName(name);
        this.name = name;
    }

    public void setPrice(double price) {
        validatePrice(price);
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cant be empty");
        }
        if (name.length() > 30) {
            throw new IllegalArgumentException("Product name cant be longer than 30 chars");
        }
    }

    private void validatePrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Product Price cant be negative");
        }
    }
}