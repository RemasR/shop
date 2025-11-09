package com.example.shop.domain.entity;

import lombok.Data;

@Data
public class Product {
    private final int id;
    private String name;
    private String description;
    private double price;

    public Product(int id, String name, String description, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

}