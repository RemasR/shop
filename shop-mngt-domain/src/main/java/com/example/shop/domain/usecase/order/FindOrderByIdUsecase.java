package com.example.shop.domain.usecase.order;

import com.example.shop.domain.model.Order;
import com.example.shop.domain.repository.OrderRepository;
import com.example.shop.domain.usecase.ValidationExecutor;

public class FindOrderByIdUsecase {

    private final OrderRepository orderRepository;
    private final ValidationExecutor<String> validationExecutor;

    public FindOrderByIdUsecase(OrderRepository orderRepository, ValidationExecutor<String> validationExecutor) {
        this.orderRepository = orderRepository;
        this.validationExecutor = validationExecutor;
    }

    public Order execute(String id) {
        validationExecutor.validateAndThrow(id);
        return orderRepository.findById(id);
    }
}