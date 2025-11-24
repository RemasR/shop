package com.example.validator.orderValidator;

import com.example.shop.domain.dto.OrderDTO;
import com.example.shop.domain.dto.OrderItemDTO;
import com.example.shop.domain.dto.SimpleViolation;
import com.example.shop.domain.entity.Order;
import com.example.shop.domain.entity.OrderItem;
import com.example.shop.domain.entity.Product;
import com.example.shop.domain.repository.ProductRepository;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.validators.order.ItemsPresenceValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ItemsPresenceValidatorTest {

    private UserRepository userRepository;
    private ProductRepository productRepository;
    private ItemsPresenceValidator validator;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        productRepository = mock(ProductRepository.class);
        validator = new ItemsPresenceValidator(userRepository, productRepository);
    }

    @Test
    void givenValidOrder_whenValidate_thenNoViolations() {
        String userId = UUID.randomUUID().toString();
        Product product = Product.builder().id("1").name("Laptop").price(1000).build();
        OrderItem item = new OrderItem(product, 2);
        Order order = Order.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .items(List.of(item))
                .build();

        when(userRepository.existsById(userId)).thenReturn(true);
        when(productRepository.existsById("1")).thenReturn(true);

        Set<SimpleViolation> violations = validator.validate(order);

        assertTrue(violations.isEmpty());
    }

    @Test
    void givenNullUserId_whenValidate_thenReturnsViolation() {
        Product product = Product.builder().id("1").build();
        OrderItem item = new OrderItem(product, 2);
        Order order = Order.builder()
                .id(UUID.randomUUID().toString())
                .userId(null)
                .items(List.of(item))
                .build();

        Set<SimpleViolation> violations = validator.validate(order);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getViolator().equals("order.userId") &&
                        v.getViolation().contains("cannot be null")));
    }

    @Test
    void givenNonExistingUser_whenValidate_thenReturnsViolation() {
        String userId = UUID.randomUUID().toString();
        Product product = Product.builder().id("1").build();
        OrderItem item = new OrderItem(product, 2);
        Order order = Order.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .items(List.of(item))
                .build();

        when(userRepository.existsById(userId)).thenReturn(false);

        Set<SimpleViolation> violations = validator.validate(order);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getViolator().equals("order.userId") &&
                        v.getViolation().contains("does not exist")));
    }

    @Test
    void givenNullItems_whenValidate_thenReturnsViolation() {
        String userId = UUID.randomUUID().toString();
        Order order = Order.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .items(null)
                .build();

        when(userRepository.existsById(userId)).thenReturn(true);

        Set<SimpleViolation> violations = validator.validate(order);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getViolator().equals("order.items") &&
                        v.getViolation().contains("at least one item")));
    }

    @Test
    void givenZeroOrNegativeQuantity_whenValidate_thenReturnsViolation() {
        String userId = UUID.randomUUID().toString();
        Product product = Product.builder().id("1").build();

        List<Integer> quantities = List.of(0, -5);
        for (Integer qty : quantities) {
            OrderItem item = new OrderItem(product, qty);
            Order order = Order.builder()
                    .id(UUID.randomUUID().toString())
                    .userId(userId)
                    .items(List.of(item))
                    .build();

            when(userRepository.existsById(userId)).thenReturn(true);
            when(productRepository.existsById("1")).thenReturn(true);

            Set<SimpleViolation> violations = validator.validate(order);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getViolator().contains("quantity") &&
                            v.getViolation().contains("must be positive")));
        }
    }

    @Test
    void givenNonExistingProduct_whenValidate_thenReturnsViolation() {
        String userId = UUID.randomUUID().toString();
        Product product = Product.builder().id("999").build();
        OrderItem item = new OrderItem(product, 2);
        Order order = Order.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .items(List.of(item))
                .build();

        when(userRepository.existsById(userId)).thenReturn(true);
        when(productRepository.existsById("999")).thenReturn(false);

        Set<SimpleViolation> violations = validator.validate(order);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getViolator().contains("productId") &&
                        v.getViolation().contains("does not exist")));
    }
}