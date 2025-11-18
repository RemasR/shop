package com.example.usecase.order;

import com.example.shop.domain.repository.OrderRepository;
import com.example.shop.domain.usecase.ValidationException;
import com.example.shop.domain.usecase.ValidationExecutor;
import com.example.shop.domain.usecase.order.DeleteOrderUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class DeleteOrderUsecaseTest {

    private OrderRepository orderRepository;
    private ValidationExecutor<String> validationExecutor;
    private DeleteOrderUsecase deleteOrderUsecase;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        validationExecutor = mock(ValidationExecutor.class);
        deleteOrderUsecase = new DeleteOrderUsecase(orderRepository, validationExecutor);
    }

    @Test
    void givenValidOrderId_whenExecute_thenOrderIsDeleted() {
        String orderId = UUID.randomUUID().toString();

        when(validationExecutor.validateAndThrow(orderId)).thenReturn(Set.of());

        deleteOrderUsecase.execute(orderId);

        verify(validationExecutor, times(1)).validateAndThrow(orderId);
        verify(orderRepository, times(1)).deleteById(orderId);
    }

    @Test
    void givenInvalidOrderId_whenExecute_thenThrowsValidationException() {
        String orderId = UUID.randomUUID().toString();

        when(validationExecutor.validateAndThrow(orderId))
                .thenThrow(new ValidationException(Set.of()));

        assertThrows(ValidationException.class, () -> deleteOrderUsecase.execute(orderId));

        verify(orderRepository, never()).deleteById(anyString());
    }
}