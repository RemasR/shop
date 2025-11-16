package com.example.shop.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class Product {
    private String id;
    private String name;
    private String description;
    private double price;
}