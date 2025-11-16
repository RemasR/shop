package com.example.valiidator.orderValidator;

import com.example.shop.domain.dto.SimpleViolation;
import com.example.shop.domain.repository.OrderRepository;
import com.example.shop.domain.validators.order.OrderExistenceValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderExistenceValidatorTest {

    private OrderRepository orderRepository;
    private OrderExistenceValidator validator;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        validator = new OrderExistenceValidator(orderRepository);
    }

    @Test
    void givenExistingOrderId_whenValidate_thenNoViolations() {
        int id = 1;
        when(orderRepository.existsById(id)).thenReturn(true);

        Set<SimpleViolation> violations = validator.validate(id);

        assertTrue(violations.isEmpty());
    }

    @Test
    void givenNonExistingOrderId_whenValidate_thenReturnsViolation() {
        int id = 999;
        when(orderRepository.existsById(id)).thenReturn(false);

        Set<SimpleViolation> violations = validator.validate(id);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());

        SimpleViolation violation = null;
        for (SimpleViolation v : violations) {
            violation = v;
            break;
        }

        assertEquals("order.id", violation.getViolator());
        assertTrue(violation.getViolation().contains("does not exist"));
    }
}