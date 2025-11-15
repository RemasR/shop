package com.example.shop.domain.usecase.order;

import com.example.shop.domain.entity.Order;
import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.OrderRepository;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.usecase.ValidationExecutor;

import java.util.List;
import java.util.UUID;

public class FindOrderByUserUsecase {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ValidationExecutor<UUID> validationExecutor;

    public FindOrderByUserUsecase(OrderRepository orderRepository,
                                  UserRepository userRepository,
                                  ValidationExecutor<UUID> validationExecutor) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.validationExecutor = validationExecutor;
    }

    public List<Order> execute(UUID userId) {
        validationExecutor.validateAndThrow(userId);
        User user = userRepository.findById(userId);
        return orderRepository.findByUser(user);
    }
}