package com.example.shop.domain.usecase.order;

import com.example.shop.domain.dto.OrderDTO;
import com.example.shop.domain.dto.OrderItemDTO;
import com.example.shop.domain.entity.*;
import com.example.shop.domain.repository.*;
import com.example.shop.domain.usecase.ValidationExecutor;

import java.util.ArrayList;
import java.util.List;

public class CreateOrderUsecase {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ValidationExecutor<OrderDTO> validationExecutor;

    public CreateOrderUsecase(OrderRepository orderRepository,
                              UserRepository userRepository,
                              ProductRepository productRepository,
                              ValidationExecutor<OrderDTO> validationExecutor) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.validationExecutor = validationExecutor;
    }

    public Order execute(OrderDTO dto) {
        validationExecutor.validateAndThrow(dto);

        User user = userRepository.findById(dto.getUserId().toString());

        List<OrderItem> orderItems = new ArrayList<>();
        double totalPrice = 0.0;

        for (OrderItemDTO itemDTO : dto.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId());
            OrderItem orderItem = new OrderItem(product, itemDTO.getQuantity());
            orderItems.add(orderItem);
            totalPrice += product.getPrice() * itemDTO.getQuantity();
        }

        Order order = Order.builder()
                .id(0)
                .user(user)
                .items(orderItems)
                .totalPrice(totalPrice)
                .status(OrderStatus.PENDING)
                .build();

        return orderRepository.save(order);
    }
}