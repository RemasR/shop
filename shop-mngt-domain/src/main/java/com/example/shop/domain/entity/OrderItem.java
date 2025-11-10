package com.example.shop.domain.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItem {
    private Product product;
    private int quantity;

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
}