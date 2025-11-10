package com.example.shop.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.*;

@Data
@Builder
@AllArgsConstructor
public class Order {
    private final int id;
    private final User user;
    private List<OrderItem> items;
    private double totalPrice;
    private OrderStatus status;

}