package com.example.usecase.order;

import com.example.shop.domain.entity.Order;
import com.example.shop.domain.entity.OrderStatus;
import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.OrderRepository;
import com.example.shop.domain.usecase.order.FindOrderByStatusUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FindOrderByStatusUsecaseTest {

    private OrderRepository orderRepository;
    private FindOrderByStatusUsecase findOrderByStatusUsecase;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        findOrderByStatusUsecase = new FindOrderByStatusUsecase(orderRepository);
    }

    @Test
    void givenValidStatus_whenExecute_thenReturnsOrdersWithThatStatus() {
        User user1 = new User(UUID.randomUUID().toString(), "Ahmad", "ahmad@test.com", "+962791234567");
        User user2 = new User(UUID.randomUUID().toString(), "Sara", "sara@test.com", "+962791234568");

        Order order1 = Order.builder()
                .id(UUID.randomUUID().toString())
                .user(user1)
                .items(new ArrayList<>())
                .totalPrice(100.0)
                .status(OrderStatus.PENDING)
                .build();

        Order order2 = Order.builder()
                .id(UUID.randomUUID().toString())
                .user(user2)
                .items(new ArrayList<>())
                .totalPrice(200.0)
                .status(OrderStatus.PENDING)
                .build();

        List<Order> expectedOrders = List.of(order1, order2);

        when(orderRepository.findByStatus(OrderStatus.PENDING)).thenReturn(expectedOrders);

        List<Order> result = findOrderByStatusUsecase.execute(OrderStatus.PENDING);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(order1));
        assertTrue(result.contains(order2));
        assertEquals(OrderStatus.PENDING, result.get(0).getStatus());
        assertEquals(OrderStatus.PENDING, result.get(1).getStatus());

        verify(orderRepository, times(1)).findByStatus(OrderStatus.PENDING);
    }

    @Test
    void givenConfirmedStatus_whenExecute_thenReturnsOnlyConfirmedOrders() {
        User user = new User(UUID.randomUUID().toString(), "Khalid", "khalid@test.com", "+962791234567");

        Order order = Order.builder()
                .id(UUID.randomUUID().toString())
                .user(user)
                .items(new ArrayList<>())
                .totalPrice(150.0)
                .status(OrderStatus.CONFIRMED)
                .build();

        when(orderRepository.findByStatus(OrderStatus.CONFIRMED)).thenReturn(List.of(order));

        List<Order> result = findOrderByStatusUsecase.execute(OrderStatus.CONFIRMED);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(OrderStatus.CONFIRMED, result.get(0).getStatus());

        verify(orderRepository, times(1)).findByStatus(OrderStatus.CONFIRMED);
    }

    @Test
    void givenShippedStatus_whenExecute_thenReturnsOnlyShippedOrders() {
        User user = new User(UUID.randomUUID().toString(), "Remas", "remas@test.com", "+962791234567");

        Order order = Order.builder()
                .id(UUID.randomUUID().toString())
                .user(user)
                .items(new ArrayList<>())
                .totalPrice(300.0)
                .status(OrderStatus.SHIPPED)
                .build();

        when(orderRepository.findByStatus(OrderStatus.SHIPPED)).thenReturn(List.of(order));

        List<Order> result = findOrderByStatusUsecase.execute(OrderStatus.SHIPPED);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(OrderStatus.SHIPPED, result.get(0).getStatus());

        verify(orderRepository, times(1)).findByStatus(OrderStatus.SHIPPED);
    }

    @Test
    void givenStatusWithNoOrders_whenExecute_thenReturnsEmptyList() {
        when(orderRepository.findByStatus(OrderStatus.DELIVERED)).thenReturn(List.of());

        List<Order> result = findOrderByStatusUsecase.execute(OrderStatus.DELIVERED);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(orderRepository, times(1)).findByStatus(OrderStatus.DELIVERED);
    }
}