package com.example.valiidator.orderValidator;

import com.example.shop.domain.dto.OrderDTO;
import com.example.shop.domain.dto.OrderItemDTO;
import com.example.shop.domain.dto.SimpleViolation;
import com.example.shop.domain.repository.ProductRepository;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.validators.order.ItemsPresenceValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void givenValidOrderDTO_whenValidate_thenNoViolations() {
        UUID userId = UUID.randomUUID();
        OrderItemDTO item = new OrderItemDTO(1, 2);
        List<OrderItemDTO> items = List.of(item);

        OrderDTO orderDTO = OrderDTO.builder()
                .userId(userId)
                .items(items)
                .build();

        when(userRepository.existsById(userId)).thenReturn(true);
        when(productRepository.existsById(1)).thenReturn(true);

        Set<SimpleViolation> violations = validator.validate(orderDTO);

        assertTrue(violations.isEmpty());
    }

    @Test
    void givenNullUserId_whenValidate_thenReturnsViolation() {
        OrderItemDTO item = new OrderItemDTO(1, 2);
        OrderDTO orderDTO = OrderDTO.builder()
                .userId(null)
                .items(List.of(item))
                .build();

        Set<SimpleViolation> violations = validator.validate(orderDTO);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getViolator().equals("order.userId") &&
                        v.getViolation().contains("cannot be null")));
    }

    @Test
    void givenNonExistingUser_whenValidate_thenReturnsViolation() {
        UUID userId = UUID.randomUUID();
        OrderItemDTO item = new OrderItemDTO(1, 2);
        OrderDTO orderDTO = OrderDTO.builder()
                .userId(userId)
                .items(List.of(item))
                .build();

        when(userRepository.existsById(userId)).thenReturn(false);

        Set<SimpleViolation> violations = validator.validate(orderDTO);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getViolator().equals("order.userId") &&
                        v.getViolation().contains("does not exist")));
    }

    @Test
    void givenNullItems_whenValidate_thenReturnsViolation() {
        UUID userId = UUID.randomUUID();
        OrderDTO orderDTO = OrderDTO.builder()
                .userId(userId)
                .items(null)
                .build();

        when(userRepository.existsById(userId)).thenReturn(true);

        Set<SimpleViolation> violations = validator.validate(orderDTO);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getViolator().equals("order.items") &&
                        v.getViolation().contains("at least one item")));
    }

    @Test
    void givenEmptyItems_whenValidate_thenReturnsViolation() {
        UUID userId = UUID.randomUUID();
        OrderDTO orderDTO = OrderDTO.builder()
                .userId(userId)
                .items(new ArrayList<>())
                .build();

        when(userRepository.existsById(userId)).thenReturn(true);

        Set<SimpleViolation> violations = validator.validate(orderDTO);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getViolator().equals("order.items") &&
                        v.getViolation().contains("at least one item")));
    }

    @Test
    void givenNonExistingProduct_whenValidate_thenReturnsViolation() {
        UUID userId = UUID.randomUUID();
        OrderItemDTO item = new OrderItemDTO(999, 2);
        OrderDTO orderDTO = OrderDTO.builder()
                .userId(userId)
                .items(List.of(item))
                .build();

        when(userRepository.existsById(userId)).thenReturn(true);
        when(productRepository.existsById(999)).thenReturn(false);

        Set<SimpleViolation> violations = validator.validate(orderDTO);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getViolator().contains("productId") &&
                        v.getViolation().contains("does not exist")));
    }

    @Test
    void givenZeroQuantity_whenValidate_thenReturnsViolation() {
        UUID userId = UUID.randomUUID();
        OrderItemDTO item = new OrderItemDTO(1, 0);
        OrderDTO orderDTO = OrderDTO.builder()
                .userId(userId)
                .items(List.of(item))
                .build();

        when(userRepository.existsById(userId)).thenReturn(true);
        when(productRepository.existsById(1)).thenReturn(true);

        Set<SimpleViolation> violations = validator.validate(orderDTO);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getViolator().contains("quantity") &&
                        v.getViolation().contains("must be positive")));
    }

    @Test
    void givenNegativeQuantity_whenValidate_thenReturnsViolation() {
        UUID userId = UUID.randomUUID();
        OrderItemDTO item = new OrderItemDTO(1, -5);
        OrderDTO orderDTO = OrderDTO.builder()
                .userId(userId)
                .items(List.of(item))
                .build();

        when(userRepository.existsById(userId)).thenReturn(true);
        when(productRepository.existsById(1)).thenReturn(true);

        Set<SimpleViolation> violations = validator.validate(orderDTO);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getViolator().contains("quantity") &&
                        v.getViolation().contains("must be positive")));
    }

}