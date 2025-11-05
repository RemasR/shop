package com.example.shop.domain.entity;

import java.util.*;

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

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}