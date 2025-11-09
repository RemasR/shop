package com.example.shop.domain.dto;

public class OrderItemDTO {
    private int productId;
    private int quantity;

    public OrderItemDTO(int productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}