package com.example.shop.domain.usecase.order;

import com.example.shop.domain.dto.OrderDTO;
import com.example.shop.domain.dto.OrderItemDTO;
import com.example.shop.domain.entity.Order;
import com.example.shop.domain.entity.OrderItem;
import com.example.shop.domain.entity.Product;
import com.example.shop.domain.repository.OrderRepository;
import com.example.shop.domain.repository.ProductRepository;
import com.example.shop.domain.usecase.ValidationExecutor;

import java.util.ArrayList;
import java.util.List;

public class UpdateOrderUsecase {


    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ValidationExecutor<String> orderExistenceValidationExecutor;
    private final ValidationExecutor<Order> orderItemPresenceValidatorExecutor;

    public UpdateOrderUsecase(OrderRepository orderRepository,
                              ProductRepository productRepository,
                              ValidationExecutor<String> orderExistenceValidationExecutor,
                              ValidationExecutor<Order> orderItemPresenceValidatorExecutor) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderExistenceValidationExecutor = orderExistenceValidationExecutor;
        this.orderItemPresenceValidatorExecutor = orderItemPresenceValidatorExecutor;
    }

    public Order execute(String orderId, OrderDTO dto) {
        orderExistenceValidationExecutor.validateAndThrow(orderId);

        Order order = orderRepository.findById(orderId);

        if (dto.getItems() != null && !dto.getItems().isEmpty()) {
            List<OrderItem> newOrderItems = new ArrayList<>();
            double newTotalPrice = 0.0;

            for (OrderItemDTO itemDTO : dto.getItems()) {
                Product product = productRepository.findById(itemDTO.getProductId());
                OrderItem orderItem = new OrderItem(product, itemDTO.getQuantity());
                newOrderItems.add(orderItem);
                newTotalPrice += product.getPrice() * itemDTO.getQuantity();
            }

            order.setItems(newOrderItems);
            order.setTotalPrice(newTotalPrice);
        }
        orderItemPresenceValidatorExecutor.validateAndThrow(order);

        return orderRepository.save(order);
    }
}