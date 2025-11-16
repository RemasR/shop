package com.example.shop.domain.validators.order;

import com.example.shop.domain.dto.SimpleViolation;
import com.example.shop.domain.repository.OrderRepository;
import com.example.shop.domain.validators.Validator;

import java.util.HashSet;
import java.util.Set;

public class OrderExistenceValidator implements Validator<String> {

    private final OrderRepository orderRepository;

    public OrderExistenceValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Set<SimpleViolation> validate(String id) {
        Set<SimpleViolation> violations = new HashSet<>();

        if (!orderRepository.existsById(id)) {
            violations.add(new SimpleViolation("order.id", "Order with ID " + id + " does not exist"));
        }

        return violations;
    }
}