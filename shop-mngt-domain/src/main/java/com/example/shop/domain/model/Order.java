package com.example.shop.domain.model;

import java.util.List;

public class Order {
    private String userId;
    private String id;
    private List<OrderItem> items;
    private double totalPrice;
    private OrderStatus status;
}