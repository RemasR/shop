package com.example.usecase.order;

import com.example.shop.domain.entity.Order;
import com.example.shop.domain.entity.OrderStatus;
import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.OrderRepository;
import com.example.shop.domain.usecase.ValidationExecutor;
import com.example.shop.domain.usecase.ValidationException;
import com.example.shop.domain.usecase.order.UpdateOrderUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UpdateOrderUsecaseTest {

    private OrderRepository orderRepository;
    private ValidationExecutor<Integer> validationExecutor;
    private UpdateOrderUsecase updateOrderUsecase;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        validationExecutor = mock(ValidationExecutor.class);
        updateOrderUsecase = new UpdateOrderUsecase(orderRepository, validationExecutor);
    }

    @Test
    void givenValidOrder_whenUpdateStatus_thenStatusIsUpdated() {
        int orderId = 1;
        User user = new User(UUID.randomUUID(), "Raslan", "raslan@test.com", "+962791234567");
        Order order = Order.builder()
                .id(orderId)
                .user(user)
                .items(new ArrayList<>())
                .totalPrice(100.0)
                .status(OrderStatus.PENDING)
                .build();

        when(validationExecutor.validateAndThrow(orderId)).thenReturn(Set.of());
        when(orderRepository.findById(orderId)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);

        Order result = updateOrderUsecase.execute(orderId, OrderStatus.CONFIRMED);

        assertEquals(OrderStatus.CONFIRMED, result.getStatus());
        verify(validationExecutor, times(1)).validateAndThrow(orderId);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void givenInvalidOrderId_whenUpdate_thenThrowsValidationException() {
        int orderId = 999;

        when(validationExecutor.validateAndThrow(orderId))
                .thenThrow(new ValidationException(Set.of()));

        assertThrows(ValidationException.class,
                () -> updateOrderUsecase.execute(orderId, OrderStatus.CONFIRMED));

        verify(orderRepository, never()).save(any());
    }
}