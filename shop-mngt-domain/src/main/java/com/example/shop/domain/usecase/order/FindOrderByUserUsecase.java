package com.example.shop.domain.usecase.order;

import com.example.shop.domain.model.Order;
import com.example.shop.domain.repository.OrderRepository;
import com.example.shop.domain.usecase.ValidationExecutor;

import java.util.List;

public class FindOrderByUserUsecase {

    private final OrderRepository orderRepository;
    private final ValidationExecutor<String> validationExecutor;

    public FindOrderByUserUsecase(OrderRepository orderRepository,
                                  ValidationExecutor<String> validationExecutor) {
        this.orderRepository = orderRepository;
        this.validationExecutor = validationExecutor;
    }

    public List<Order> execute(String userId) {
        validationExecutor.validateAndThrow(userId);
        return orderRepository.findByUserId(userId);
    }
}