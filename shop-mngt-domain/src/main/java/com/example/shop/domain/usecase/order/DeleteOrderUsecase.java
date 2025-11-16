package com.example.shop.domain.usecase.order;

import com.example.shop.domain.repository.OrderRepository;
import com.example.shop.domain.usecase.ValidationExecutor;

public class DeleteOrderUsecase {

    private final OrderRepository orderRepository;
    private final ValidationExecutor<String> validationExecutor;

    public DeleteOrderUsecase(OrderRepository orderRepository, ValidationExecutor<String> validationExecutor) {
        this.orderRepository = orderRepository;
        this.validationExecutor = validationExecutor;
    }

    public void execute(String id) {
        validationExecutor.validateAndThrow(id);
        orderRepository.deleteById(id);
    }
}