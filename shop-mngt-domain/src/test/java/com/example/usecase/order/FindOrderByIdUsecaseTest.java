package com.example.usecase.order;

import com.example.shop.domain.model.Order;
import com.example.shop.domain.model.OrderStatus;
import com.example.shop.domain.repository.OrderRepository;
import com.example.shop.domain.usecase.ValidationException;
import com.example.shop.domain.usecase.ValidationExecutor;
import com.example.shop.domain.usecase.order.FindOrderByIdUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FindOrderByIdUsecaseTest {

    private OrderRepository orderRepository;
    private ValidationExecutor<String> validationExecutor;
    private FindOrderByIdUsecase findOrderByIdUsecase;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        validationExecutor = mock(ValidationExecutor.class);
        findOrderByIdUsecase = new FindOrderByIdUsecase(orderRepository, validationExecutor);
    }

    @Test
    void givenValidOrderId_whenExecute_thenReturnsOrder() {
        String orderId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        Order order = Order.builder()
                .id(orderId)
                .userId(userId)
                .items(new ArrayList<>())
                .totalPrice(100.0)
                .status(OrderStatus.PENDING)
                .build();

        when(validationExecutor.validateAndThrow(orderId)).thenReturn(Set.of());
        when(orderRepository.findById(orderId)).thenReturn(order);

        Order result = findOrderByIdUsecase.execute(orderId);

        assertNotNull(result);
        assertEquals(orderId, result.getId());
        assertEquals(userId, result.getUserId());

        verify(validationExecutor, times(1)).validateAndThrow(orderId);
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void givenInvalidOrderId_whenExecute_thenThrowsValidationException() {
        String orderId = UUID.randomUUID().toString();

        when(validationExecutor.validateAndThrow(orderId))
                .thenThrow(new ValidationException(Set.of()));

        assertThrows(ValidationException.class, () -> findOrderByIdUsecase.execute(orderId));

        verify(orderRepository, never()).findById(anyString());
    }
}