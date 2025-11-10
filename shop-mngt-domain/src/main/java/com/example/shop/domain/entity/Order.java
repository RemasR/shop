package com.example.shop.domain.entity;

import lombok.Builder;
import lombok.Data;

import java.util.*;

@Data
@Builder
public class Order {
    private final int id;
    private final User user;
    private List<OrderItem> items;
    private double totalPrice;
    private OrderStatus status;


    public Order(int id, User user) {
        this.id = id;
        this.user = user;
        this.items = new ArrayList<>();
        this.totalPrice = 0.0;
        this.status = OrderStatus.PENDING;
    }
}