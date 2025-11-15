package com.example.shop.domain.usecase.order;

import com.example.shop.domain.entity.Order;
import com.example.shop.domain.repository.OrderRepository;
import com.example.shop.domain.usecase.ValidationExecutor;

public class FindOrderByIdUsecase {

    private final OrderRepository orderRepository;
    private final ValidationExecutor<Integer> validationExecutor;

    public FindOrderByIdUsecase(OrderRepository orderRepository, ValidationExecutor<Integer> validationExecutor) {
        this.orderRepository = orderRepository;
        this.validationExecutor = validationExecutor;
    }

    public Order execute(int id) {
        validationExecutor.validateAndThrow(id);
        return orderRepository.findById(id);
    }
}