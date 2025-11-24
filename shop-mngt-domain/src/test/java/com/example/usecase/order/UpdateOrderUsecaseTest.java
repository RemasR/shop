package com.example.usecase.order;

import com.example.shop.domain.dto.OrderDTO;
import com.example.shop.domain.dto.OrderItemDTO;
import com.example.shop.domain.entity.*;
import com.example.shop.domain.repository.OrderRepository;
import com.example.shop.domain.repository.ProductRepository;
import com.example.shop.domain.usecase.ValidationException;
import com.example.shop.domain.usecase.ValidationExecutor;
import com.example.shop.domain.usecase.order.UpdateOrderUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UpdateOrderUsecaseTest {

    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    private ValidationExecutor<String> orderExistenceValidationExecutor;
    private ValidationExecutor<Order> orderItemPresenceValidatorExecutor;
    private UpdateOrderUsecase updateOrderUsecase;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        productRepository = mock(ProductRepository.class);
        orderExistenceValidationExecutor = mock(ValidationExecutor.class);
        orderItemPresenceValidatorExecutor = mock(ValidationExecutor.class);
        updateOrderUsecase = new UpdateOrderUsecase(
                orderRepository,
                productRepository,
                orderExistenceValidationExecutor,
                orderItemPresenceValidatorExecutor
        );
    }

    @Test
    void givenValidOrderDTO_whenUpdate_thenOrderItemsAndTotalPriceAreUpdated() {
        String orderId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();

        Product newProduct1 = Product.builder()
                .id("2")
                .name("Laptop")
                .price(1000.0)
                .build();
        Product newProduct2 = Product.builder()
                .id("3")
                .name("Mouse")
                .price(50.0)
                .build();

        OrderItemDTO itemDTO1 = new OrderItemDTO("2", 2);
        OrderItemDTO itemDTO2 = new OrderItemDTO("3", 3);
        OrderDTO dto = OrderDTO.builder()
                .userId(userId)
                .items(List.of(itemDTO1, itemDTO2))
                .build();

        Order existingOrder = Order.builder()
                .id(orderId)
                .userId(userId)
                .items(new ArrayList<>())
                .totalPrice(0.0)
                .status(OrderStatus.PENDING)
                .build();

        when(orderExistenceValidationExecutor.validateAndThrow(orderId)).thenReturn(Set.of());
        when(orderRepository.findById(orderId)).thenReturn(existingOrder);
        when(productRepository.findById("2")).thenReturn(newProduct1);
        when(productRepository.findById("3")).thenReturn(newProduct2);
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        Order result = updateOrderUsecase.execute(orderId, dto);

        ArgumentCaptor<String> orderIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);

        verify(orderExistenceValidationExecutor).validateAndThrow(orderIdCaptor.capture());
        verify(orderItemPresenceValidatorExecutor).validateAndThrow(orderCaptor.capture());
        verify(orderRepository).save(orderCaptor.capture());

        assertEquals(orderId, orderIdCaptor.getValue());

        Order validatedOrder = orderCaptor.getAllValues().get(0);
        Order savedOrder = orderCaptor.getAllValues().get(1);

        assertEquals(2, validatedOrder.getItems().size());
        assertEquals(2150.0, validatedOrder.getTotalPrice());

        assertEquals(2, savedOrder.getItems().size());
        assertEquals(2150.0, savedOrder.getTotalPrice());
        assertEquals(OrderStatus.PENDING, savedOrder.getStatus());
    }


    @Test
    void givenInvalidOrderId_whenUpdate_thenThrowsValidationException() {
        String orderId = UUID.randomUUID().toString();
        OrderDTO dto = OrderDTO.builder()
                .userId(UUID.randomUUID().toString())
                .items(List.of(new OrderItemDTO("1", 2)))
                .build();

        doThrow(new ValidationException(Set.of()))
                .when(orderExistenceValidationExecutor).validateAndThrow(orderId);

        assertThrows(ValidationException.class,
                () -> updateOrderUsecase.execute(orderId, dto));

        verify(orderExistenceValidationExecutor, times(1)).validateAndThrow(orderId);
        verify(orderItemPresenceValidatorExecutor, never()).validateAndThrow(any());
        verify(orderRepository, never()).findById(any());
        verify(orderRepository, never()).save(any());
    }
}
