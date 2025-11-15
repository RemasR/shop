package com.example.shop.domain.usecase.order;

import com.example.shop.domain.entity.Order;
import com.example.shop.domain.repository.OrderRepository;

import java.util.List;

public class ListAllOrderUsecase {

    private final OrderRepository orderRepository;

    public ListAllOrderUsecase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> execute() {
        return orderRepository.findAllOrders();
    }
}