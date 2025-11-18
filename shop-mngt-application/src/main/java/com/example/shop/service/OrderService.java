package com.example.shop.service;

import com.example.shop.domain.dto.OrderDTO;
import com.example.shop.domain.entity.Order;
import com.example.shop.domain.entity.OrderStatus;
import com.example.shop.domain.usecase.order.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class OrderService {
    private final CreateOrderUsecase createOrderUsecase;
    private final UpdateOrderUsecase updateOrderUsecase;
    private final DeleteOrderUsecase deleteOrderUsecase;
    private final FindOrderByIdUsecase findOrderByIdUsecase;
    private final FindOrderByStatusUsecase findOrderByStatusUsecase;
    private final FindOrderByUserUsecase findOrderByUserUsecase;
    private final ListAllOrderUsecase listAllOrderUsecase;
    public Order createOrder(OrderDTO dto) {
        return createOrderUsecase.execute(dto);
    }

    public Order getOrderById(String id) {
        return findOrderByIdUsecase.execute(id);
    }

    public Order updateOrder(String id, OrderDTO dto) {
        return updateOrderUsecase.execute(id, dto);
    }

    public void cancelOrder(String id) {
        deleteOrderUsecase.execute(id);
    }

    public List<Order> getAllOrders() {
        return listAllOrderUsecase.execute();
    }

    public List<Order> getUserOrders(String userId) {
        return findOrderByUserUsecase.execute(userId);
    }

    public List<Order> getOrderByStatus(OrderStatus orderStatus) {
        return findOrderByStatusUsecase.execute(orderStatus);
    }
}