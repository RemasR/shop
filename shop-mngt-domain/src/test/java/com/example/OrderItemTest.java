package com.example;

import com.example.shop.domain.entity.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderItemTest {
    private OrderItem orderItem;
    private Product product, newProduct;
    @BeforeEach
    void setUp() {
        product = new Product(1, "Shoe",
                "Black Shoes", 13.5);
    }

    @Test
    public void givenValidProductAndQuantity_whenCreateOrderItem_thenOrderItemIsCreated() {
        orderItem = new OrderItem(product, 4);

        assertEquals(product, orderItem.getProduct());
        assertEquals(4, orderItem.getQuantity());
    }

    @Test
    public void givenValidQuantity_whenSetQuantity_thenQuantityIsUpdated() {
    orderItem = new OrderItem(product,4);
    orderItem.setQuantity(1);
    assertEquals(1, orderItem.getQuantity());
    }
}