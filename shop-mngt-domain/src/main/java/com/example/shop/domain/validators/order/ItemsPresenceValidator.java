package com.example.shop.domain.validators.order;

import com.example.shop.domain.dto.OrderDTO;
import com.example.shop.domain.dto.OrderItemDTO;
import com.example.shop.domain.dto.SimpleViolation;
import com.example.shop.domain.entity.Order;
import com.example.shop.domain.entity.OrderItem;
import com.example.shop.domain.repository.ProductRepository;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.validators.Validator;

import java.util.HashSet;
import java.util.Set;

public class ItemsPresenceValidator implements Validator<Order> {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ItemsPresenceValidator(UserRepository userRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Set<SimpleViolation> validate(Order order) {
        Set<SimpleViolation> violations = new HashSet<>();

        if (order.getUserId() == null) {
            violations.add(new SimpleViolation("order.userId", "User ID cannot be null"));
        } else if (!userRepository.existsById(order.getUserId())) {
            violations.add(new SimpleViolation("order.userId", "User does not exist"));
        }

        if (order.getItems() == null || order.getItems().isEmpty()) {
            violations.add(new SimpleViolation("order.items", "Order must have at least one item"));
        } else {
            for (int i = 0; i < order.getItems().size(); i++) {
                OrderItem item = order.getItems().get(i);

                if (!productRepository.existsById(item.getProduct().getId())) {
                    violations.add(new SimpleViolation("order.items[" + i + "].productId",
                            "Product with ID " + item.getProduct().getId()+ " does not exist"));
                }

                if (item.getQuantity() <= 0) {
                    violations.add(new SimpleViolation("order.items[" + i + "].quantity",
                            "Quantity must be positive"));
                }
            }
        }

        return violations;
    }
}