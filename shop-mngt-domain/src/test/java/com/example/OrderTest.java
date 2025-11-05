package com.example;

import com.example.shop.domain.entity.*;
import com.example.shop.domain.entity.Order;
import org.junit.jupiter.api.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {
    private Order order;
    private User user;
    private Product product1, product2;
    private OrderItem orderItem1, orderItem2;

    @BeforeEach
    void setUp() {
        UUID userId = UUID.randomUUID();
        user = new User(userId, "Khalid", "khalid@gmail.com", "+962794128947");
    }

    @Test
    public void givenValidUserAndId_whenCreateOrder_thenOrderIsCreated() {
        order = new Order(1, user);

        assertNotNull(order);
        assertEquals(1, order.getId());
        assertEquals(user, order.getUser());
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertEquals(0.0, order.getTotalPrice());
        assertTrue(order.getItems().isEmpty());
    }

    @Test
    public void givenNullUser_whenCreateOrder_thenThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Order(1, null);
        });
    }

    @Test
    public void givenValidOrderItem_whenAddItem_thenItemIsAdded() {
        product1 = new Product(1, "T-Shirt", "Black T-Shirt", 10.0);

        order = new Order(1, user);
        orderItem1 = new OrderItem(product1, 2);

        order.addItem(orderItem1);

        assertEquals(1, order.getItems().size());
        assertTrue(order.getItems().contains(orderItem1));
        assertEquals(20.0, order.getTotalPrice());
    }

    @Test
    public void givenNullItem_whenAddItem_thenThrowsException() {
        order = new Order(1, user);

        assertThrows(IllegalArgumentException.class, () -> {
            order.addItem(null);
        });
    }

    @Test
    public void givenMultipleItems_whenAddItem_thenTotalPriceIsCalculated() {
        product1 = new Product(1, "T-Shirt", "Black T-Shirt", 10.0);
        product2 = new Product(2, "Jeans", "Blue Jeans", 10.0);

        order = new Order(1, user);
        orderItem1 = new OrderItem(product1, 2);
        orderItem2 = new OrderItem(product2, 3);

        order.addItem(orderItem1);
        order.addItem(orderItem2);

        assertEquals(2, order.getItems().size());
        assertEquals(50.0, order.getTotalPrice());
    }

    @Test
    public void givenValidStatus_whenSetStatus_thenStatusIsUpdated() {
        order = new Order(1, user);

        order.setStatus(OrderStatus.CONFIRMED);

        assertEquals(OrderStatus.CONFIRMED, order.getStatus());

        order.setStatus(OrderStatus.SHIPPED);

        assertEquals(OrderStatus.SHIPPED, order.getStatus());
    }
}