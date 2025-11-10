package com.example.shop.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemDTO {
    private int productId;
    private int quantity;

    public OrderItemDTO(int productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}