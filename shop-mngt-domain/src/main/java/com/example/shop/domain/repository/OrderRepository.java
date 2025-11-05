package com.example.shop.domain.repository;

import com.example.shop.domain.entity.*;

import java.util.*;

public interface OrderRepository {

    Order save(Order orders);

    Order findById(int id);

    List<Order> findAllOrders();

    List<Order> findByUser(User user);

    List<Order> findByStatus(OrderStatus status);

    void deleteById(int id);

    boolean existsById(int id);
}