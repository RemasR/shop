package com.example.shop.domain.dto;

import lombok.Data;

@Data
public class OrderItemDTO {
    private int productId;
    private int quantity;

    public OrderItemDTO(int productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}