package com.example.usecase.order;

import com.example.shop.domain.dto.OrderDTO;
import com.example.shop.domain.dto.OrderItemDTO;
import com.example.shop.domain.entity.*;
import com.example.shop.domain.repository.OrderRepository;
import com.example.shop.domain.repository.ProductRepository;
import com.example.shop.domain.usecase.ValidationExecutor;
import com.example.shop.domain.usecase.ValidationException;
import com.example.shop.domain.usecase.order.UpdateOrderUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    private ValidationExecutor<OrderDTO> orderDTOValidationExecutor;
    private UpdateOrderUsecase updateOrderUsecase;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        productRepository = mock(ProductRepository.class);
        orderExistenceValidationExecutor = mock(ValidationExecutor.class);
        orderDTOValidationExecutor = mock(ValidationExecutor.class);
        updateOrderUsecase = new UpdateOrderUsecase(
                orderRepository,
                productRepository,
                orderExistenceValidationExecutor,
                orderDTOValidationExecutor
        );
    }

    @Test
    void givenValidOrderDTO_whenUpdate_thenOrderItemsAndTotalPriceAreUpdated() {
        String orderId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        User user = new User(userId, "Raslan", "raslan@test.com", "+962791234567");

        Product oldProduct = Product.builder()
                .id("1")
                .name("Old Product")
                .price(50.0)
                .build();
        OrderItem oldItem = new OrderItem(oldProduct, 1);
        Order existingOrder = Order.builder()
                .id(orderId)
                .user(user)
                .items(new ArrayList<>(List.of(oldItem)))
                .totalPrice(50.0)
                .status(OrderStatus.PENDING)
                .build();

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

        when(orderExistenceValidationExecutor.validateAndThrow(orderId)).thenReturn(Set.of());
        when(orderDTOValidationExecutor.validateAndThrow(dto)).thenReturn(Set.of());
        when(orderRepository.findById(orderId)).thenReturn(existingOrder);
        when(productRepository.findById("2")).thenReturn(newProduct1);
        when(productRepository.findById("3")).thenReturn(newProduct2);
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        Order result = updateOrderUsecase.execute(orderId, dto);

        assertNotNull(result);
        assertEquals(2, result.getItems().size());
        assertEquals(2150.0, result.getTotalPrice());
        assertEquals("Laptop", result.getItems().get(0).getProduct().getName());
        assertEquals("Mouse", result.getItems().get(1).getProduct().getName());

        verify(orderExistenceValidationExecutor, times(1)).validateAndThrow(orderId);
        verify(orderDTOValidationExecutor, times(1)).validateAndThrow(dto);
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(existingOrder);
    }

    @Test
    void givenInvalidOrderId_whenUpdate_thenThrowsValidationException() {
        String orderId = UUID.randomUUID().toString();
        OrderDTO dto = OrderDTO.builder()
                .userId(UUID.randomUUID().toString())
                .items(List.of(new OrderItemDTO("1", 2)))
                .build();

        when(orderExistenceValidationExecutor.validateAndThrow(orderId))
                .thenThrow(new ValidationException(Set.of()));

        assertThrows(ValidationException.class,
                () -> updateOrderUsecase.execute(orderId, dto));

        verify(orderExistenceValidationExecutor, times(1)).validateAndThrow(orderId);
        verify(orderDTOValidationExecutor, never()).validateAndThrow(any());
        verify(orderRepository, never()).findById(any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void givenNullItems_whenUpdate_thenOrderRemainsUnchanged() {
        String orderId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        User user = new User(userId, "Sara", "sara@test.com", "+962791234567");

        Product existingProduct = Product.builder()
                .id("1")
                .name("Existing Product")
                .price(100.0)
                .build();
        OrderItem existingItem = new OrderItem(existingProduct, 2);

        Order existingOrder = Order.builder()
                .id(orderId)
                .user(user)
                .items(new ArrayList<>(List.of(existingItem)))
                .totalPrice(200.0)
                .status(OrderStatus.CONFIRMED)
                .build();

        OrderDTO dto = OrderDTO.builder()
                .userId(userId)
                .items(null)
                .build();

        when(orderExistenceValidationExecutor.validateAndThrow(orderId)).thenReturn(Set.of());
        when(orderDTOValidationExecutor.validateAndThrow(dto)).thenReturn(Set.of());
        when(orderRepository.findById(orderId)).thenReturn(existingOrder);
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        Order result = updateOrderUsecase.execute(orderId, dto);

        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals(200.0, result.getTotalPrice());
        assertEquals(OrderStatus.CONFIRMED, result.getStatus());

        verify(orderRepository, times(1)).save(existingOrder);
    }
}