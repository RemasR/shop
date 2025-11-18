package com.example.shop.domain.repository;

import com.example.shop.domain.entity.Order;
import com.example.shop.domain.entity.OrderStatus;
import com.example.shop.domain.entity.User;

import java.util.List;

public interface OrderRepository {

    Order save(Order orders);

    Order findById(String id);

    List<Order> findAllOrders();

    List<Order> findByUser(User user);

    List<Order> findByStatus(OrderStatus status);

    void deleteById(String id);

    boolean existsById(String id);
}