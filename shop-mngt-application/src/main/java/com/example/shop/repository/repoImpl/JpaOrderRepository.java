package com.example.shop.repository;

import com.example.shop.domain.entity.Order;
import com.example.shop.domain.entity.OrderStatus;
import com.example.shop.domain.repository.OrderRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JpaOrderRepository implements OrderRepository {

    private final Map<String, Order> orderStore = new HashMap<>();

    @Override
    public Order save(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        orderStore.put(order.getId(), order);
        return order;
    }

    @Override
    public Order findById(String id) {
        return orderStore.get(id);
    }

    @Override
    public List<Order> findAllOrders() {
        return new ArrayList<>(orderStore.values());
    }

    @Override
    public List<Order> findByUserId(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        List<Order> result = new ArrayList<>();
        for (Order order : orderStore.values()) {
            if (order.getUserId().equals(userId)) {
                result.add(order);
            }
        }
        return result;
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }

        List<Order> result = new ArrayList<>();
        for (Order order : orderStore.values()) {
            if (order.getStatus() == status) {
                result.add(order);
            }
        }
        return result;
    }

    @Override
    public void deleteById(String id) {
        orderStore.remove(id);
    }

    @Override
    public boolean existsById(String id) {
        return orderStore.containsKey(id);
    }
}