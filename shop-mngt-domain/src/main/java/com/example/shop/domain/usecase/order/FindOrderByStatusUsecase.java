package com.example.shop.domain.usecase.order;

import com.example.shop.domain.entity.Order;
import com.example.shop.domain.entity.OrderStatus;
import com.example.shop.domain.repository.OrderRepository;

import java.util.List;

public class FindOrderByStatusUsecase {

    private final OrderRepository orderRepository;

    public FindOrderByStatusUsecase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> execute(OrderStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        return orderRepository.findByStatus(status);
    }
}