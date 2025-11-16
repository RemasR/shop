package com.example.shop.domain.repository;

import com.example.shop.domain.entity.*;

import java.util.*;

public interface OrderRepository {

    Order save(Order orders);

    Order findById(String id);

    List<Order> findAllOrders();

    List<Order> findByUser(User user);

    List<Order> findByStatus(OrderStatus status);

    void deleteById(String id);

    boolean existsById(String id);
}