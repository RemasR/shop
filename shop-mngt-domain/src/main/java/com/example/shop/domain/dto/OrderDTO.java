package com.example.shop.domain.dto;

import java.util.List;
import java.util.UUID;

public class OrderDTO {
    private UUID userId;
    private List<OrderItemDTO> items;

    public OrderDTO(UUID userId, List<OrderItemDTO> items) {
        this.userId = userId;
        this.items = items;
    }

    public UUID getUserId() {
        return userId;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }
}