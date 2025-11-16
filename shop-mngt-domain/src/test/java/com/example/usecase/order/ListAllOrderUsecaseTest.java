package com.example.usecase.order;

import com.example.shop.domain.entity.Order;
import com.example.shop.domain.entity.OrderStatus;
import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.OrderRepository;
import com.example.shop.domain.usecase.order.ListAllOrderUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ListAllOrderUsecaseTest {

    private OrderRepository orderRepository;
    private ListAllOrderUsecase listAllOrderUsecase;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        listAllOrderUsecase = new ListAllOrderUsecase(orderRepository);
    }

    @Test
    void givenOrdersExist_whenExecute_thenReturnsAllOrders() {
        User user1 = new User(UUID.randomUUID(), "Remas", "remas@test.com", "+962791234567");
        User user2 = new User(UUID.randomUUID(), "Hamza", "hamza@test.com", "+962791234568");

        Order order1 = Order.builder().id(1).user(user1).items(new ArrayList<>())
                .totalPrice(100.0).status(OrderStatus.PENDING).build();
        Order order2 = Order.builder().id(2).user(user2).items(new ArrayList<>())
                .totalPrice(200.0).status(OrderStatus.CONFIRMED).build();

        List<Order> orders = List.of(order1, order2);

        when(orderRepository.findAllOrders()).thenReturn(orders);

        List<Order> result = listAllOrderUsecase.execute();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(order1));
        assertTrue(result.contains(order2));

        verify(orderRepository, times(1)).findAllOrders();
    }

    @Test
    void givenNoOrders_whenExecute_thenReturnsEmptyList() {
        when(orderRepository.findAllOrders()).thenReturn(List.of());

        List<Order> result = listAllOrderUsecase.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(orderRepository, times(1)).findAllOrders();
    }
}