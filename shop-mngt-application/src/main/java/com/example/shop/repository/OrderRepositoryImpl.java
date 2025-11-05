package com.example.shop.repository;

import com.example.shop.domain.entity.Order;
import com.example.shop.domain.entity.OrderStatus;
import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.OrderRepository;

import java.util.*;

public class OrderRepositoryImpl implements OrderRepository {

    private final Map<Integer, Order> orderStore = new HashMap<>();

    @Override
    public Order save(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        orderStore.put(order.getId(), order);
        return order;
    }

    @Override
    public Order findById(int id) {
        return orderStore.get(id);
    }

    @Override
    public List<Order> findAllOrders() {
        return new ArrayList<>(orderStore.values());
    }

    @Override
    public List<Order> findByUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        List<Order> result = new ArrayList<>();
        for (Order order : orderStore.values()) {
            if (order.getUser().getId().equals(user.getId())) {
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
    public void deleteById(int id) {
        orderStore.remove(id);
    }

    @Override
    public boolean existsById(int id) {
        return orderStore.containsKey(id);
    }
}
