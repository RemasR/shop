package com.example.shop.domain.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class OrderDTO {
    private UUID userId;
    private List<OrderItemDTO> items;

    public OrderDTO(UUID userId, List<OrderItemDTO> items) {
        this.userId = userId;
        this.items = items;
    }
}