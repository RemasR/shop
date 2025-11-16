package com.example.usecase.order;

import com.example.shop.domain.entity.Order;
import com.example.shop.domain.entity.OrderStatus;
import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.OrderRepository;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.usecase.ValidationExecutor;
import com.example.shop.domain.usecase.ValidationException;
import com.example.shop.domain.usecase.order.FindOrderByUserUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

    public class FindOrderByUserUsecaseTest {

    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private ValidationExecutor<String> validationExecutor;
    private FindOrderByUserUsecase findOrderByUserUsecase;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        userRepository = mock(UserRepository.class);
        validationExecutor = mock(ValidationExecutor.class);
        findOrderByUserUsecase = new FindOrderByUserUsecase(orderRepository, userRepository, validationExecutor);
    }

    @Test
    void givenValidUserId_whenExecute_thenReturnsAllOrdersForUser() {
        String userId = UUID.randomUUID().toString();
        User user = new User(userId, "Raslan", "raslan@test.com", "+962791234567");

        Order order1 = Order.builder()
                .id(UUID.randomUUID().toString())
                .user(user)
                .items(new ArrayList<>())
                .totalPrice(100.0)
                .status(OrderStatus.PENDING)
                .build();

        Order order2 = Order.builder()
                .id(UUID.randomUUID().toString())
                .user(user)
                .items(new ArrayList<>())
                .totalPrice(200.0)
                .status(OrderStatus.CONFIRMED)
                .build();

        List<Order> expectedOrders = List.of(order1, order2);

        when(validationExecutor.validateAndThrow(userId)).thenReturn(Set.of());
        when(userRepository.findById(userId)).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(expectedOrders);

        List<Order> result = findOrderByUserUsecase.execute(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(order1));
        assertTrue(result.contains(order2));
        result.forEach(order -> assertEquals(userId, order.getUser().getId()));

        verify(validationExecutor, times(1)).validateAndThrow(userId);
        verify(userRepository, times(1)).findById(userId);
        verify(orderRepository, times(1)).findByUser(user);
    }

    @Test
    void givenUserWithNoOrders_whenExecute_thenReturnsEmptyList() {
        String userId = UUID.randomUUID().toString();
        User user = new User(userId, "Khalid", "khalid@test.com", "+962791234567");

        when(validationExecutor.validateAndThrow(userId)).thenReturn(Set.of());
        when(userRepository.findById(userId)).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(List.of());

        List<Order> result = findOrderByUserUsecase.execute(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(validationExecutor, times(1)).validateAndThrow(userId);
        verify(userRepository, times(1)).findById(userId);
        verify(orderRepository, times(1)).findByUser(user);
    }

    @Test
    void givenInvalidUserId_whenExecute_thenThrowsValidationException() {
        String userId = UUID.randomUUID().toString();

        when(validationExecutor.validateAndThrow(userId))
                .thenThrow(new ValidationException(Set.of()));

        assertThrows(ValidationException.class, () -> findOrderByUserUsecase.execute(userId));

        verify(validationExecutor, times(1)).validateAndThrow(userId);
        verify(userRepository, never()).findById(any());
        verify(orderRepository, never()).findByUser(any());
    }

    @Test
    void givenNullUserId_whenExecute_thenThrowsValidationException() {
        when(validationExecutor.validateAndThrow(null))
                .thenThrow(new ValidationException(Set.of()));

        assertThrows(ValidationException.class, () -> findOrderByUserUsecase.execute(null));

        verify(validationExecutor, times(1)).validateAndThrow(null);
        verify(userRepository, never()).findById(any());
        verify(orderRepository, never()).findByUser(any());
    }

    @Test
    void givenUserWithMultipleOrderStatuses_whenExecute_thenReturnsAllOrdersRegardlessOfStatus() {
        String userId = UUID.randomUUID().toString();
        User user = new User(userId, "Ahmad", "ahmad@test.com", "+962791234567");

        Order pendingOrder = Order.builder()
                .id(UUID.randomUUID().toString())
                .user(user)
                .items(new ArrayList<>())
                .totalPrice(100.0)
                .status(OrderStatus.PENDING)
                .build();

        Order confirmedOrder = Order.builder()
                .id(UUID.randomUUID().toString())
                .user(user)
                .items(new ArrayList<>())
                .totalPrice(200.0)
                .status(OrderStatus.CONFIRMED)
                .build();

        Order deliveredOrder = Order.builder()
                .id(UUID.randomUUID().toString())
                .user(user)
                .items(new ArrayList<>())
                .totalPrice(300.0)
                .status(OrderStatus.DELIVERED)
                .build();

        List<Order> allOrders = List.of(pendingOrder, confirmedOrder, deliveredOrder);

        when(validationExecutor.validateAndThrow(userId)).thenReturn(Set.of());
        when(userRepository.findById(userId)).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(allOrders);

        List<Order> result = findOrderByUserUsecase.execute(userId);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains(pendingOrder));
        assertTrue(result.contains(confirmedOrder));
        assertTrue(result.contains(deliveredOrder));

        verify(validationExecutor, times(1)).validateAndThrow(userId);
        verify(userRepository, times(1)).findById(userId);
        verify(orderRepository, times(1)).findByUser(user);
    }

}