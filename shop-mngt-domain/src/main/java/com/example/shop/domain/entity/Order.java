package com.example.shop.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Order {
    private final User user;
    private String id;
    private List<OrderItem> items;
    private double totalPrice;
    private OrderStatus status;

}