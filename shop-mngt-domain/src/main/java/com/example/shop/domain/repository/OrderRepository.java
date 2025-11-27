package com.example.shop.domain.repository;

import com.example.shop.domain.model.Order;
import com.example.shop.domain.model.OrderStatus;

import java.util.List;

public interface OrderRepository {

    Order save(Order orders);

    Order findById(String id);

    List<Order> findAllOrders();

    List<Order> findByUserId(String userId);

    List<Order> findByStatus(OrderStatus status);

    void deleteById(String id);

    boolean existsById(String id);
}