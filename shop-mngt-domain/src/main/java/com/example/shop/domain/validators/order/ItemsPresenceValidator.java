package com.example.shop.domain.validators.order;

import com.example.shop.domain.dto.OrderDTO;
import com.example.shop.domain.dto.OrderItemDTO;
import com.example.shop.domain.dto.SimpleViolation;
import com.example.shop.domain.repository.ProductRepository;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.validators.Validator;

import java.util.HashSet;
import java.util.Set;

public class ItemsPresenceValidator implements Validator<OrderDTO> {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ItemsPresenceValidator(UserRepository userRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Set<SimpleViolation> validate(OrderDTO orderDTO) {
        Set<SimpleViolation> violations = new HashSet<>();

        if (orderDTO.getUserId() == null) {
            violations.add(new SimpleViolation("order.userId", "User ID cannot be null"));
        } else if (!userRepository.existsById(orderDTO.getUserId())) {
            violations.add(new SimpleViolation("order.userId", "User does not exist"));
        }

        if (orderDTO.getItems() == null || orderDTO.getItems().isEmpty()) {
            violations.add(new SimpleViolation("order.items", "Order must have at least one item"));
        } else {
            for (int i = 0; i < orderDTO.getItems().size(); i++) {
                OrderItemDTO item = orderDTO.getItems().get(i);

                if (!productRepository.existsById(item.getProductId())) {
                    violations.add(new SimpleViolation("order.items[" + i + "].productId",
                            "Product with ID " + item.getProductId() + " does not exist"));
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