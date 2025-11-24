package com.example.shop.domain.usecase.order;

import com.example.shop.domain.dto.OrderDTO;
import com.example.shop.domain.dto.OrderItemDTO;
import com.example.shop.domain.entity.*;
import com.example.shop.domain.repository.OrderRepository;
import com.example.shop.domain.repository.ProductRepository;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.usecase.ValidationExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreateOrderUsecase {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ValidationExecutor<Order> validationExecutor;

    public CreateOrderUsecase(OrderRepository orderRepository,
                              ProductRepository productRepository,
                              ValidationExecutor<Order> validationExecutor) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.validationExecutor = validationExecutor;
    }

    public Order execute(OrderDTO dto) {

        List<OrderItem> orderItems = new ArrayList<>();
        double totalPrice = 0.0;

        for (OrderItemDTO itemDTO : dto.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId());
            OrderItem orderItem = new OrderItem(product, itemDTO.getQuantity());
            orderItems.add(orderItem);
            totalPrice += product.getPrice() * itemDTO.getQuantity();
        }

        Order order = Order.builder()
                .id(UUID.randomUUID().toString())
                .userId(dto.getUserId())
                .items(orderItems)
                .totalPrice(totalPrice)
                .status(OrderStatus.PENDING)
                .build();
        validationExecutor.validateAndThrow(order);
        return orderRepository.save(order);
    }
}