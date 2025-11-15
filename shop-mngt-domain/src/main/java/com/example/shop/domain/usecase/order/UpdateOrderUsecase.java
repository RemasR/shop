package com.example.shop.domain.usecase.order;

import com.example.shop.domain.entity.Order;
import com.example.shop.domain.entity.OrderStatus;
import com.example.shop.domain.repository.OrderRepository;
import com.example.shop.domain.usecase.ValidationExecutor;

public class UpdateOrderUsecase {

    private final OrderRepository orderRepository;
    private final ValidationExecutor<Integer> validationExecutor;

    public UpdateOrderUsecase(OrderRepository orderRepository, ValidationExecutor<Integer> validationExecutor) {
        this.orderRepository = orderRepository;
        this.validationExecutor = validationExecutor;
    }

    public Order execute(int orderId, OrderStatus newStatus) {
        validationExecutor.validateAndThrow(orderId);

        Order order = orderRepository.findById(orderId);
        order.setStatus(newStatus);

        return orderRepository.save(order);
    }
}