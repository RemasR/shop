package com.example.valiidator.productValidator;

import com.example.shop.domain.dto.SimpleViolation;
import com.example.shop.domain.repository.ProductRepository;
import com.example.shop.domain.validators.product.ProductExistenceValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductExistenceValidatorTest {

    private ProductRepository productRepository;
    private ProductExistenceValidator validator;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        validator = new ProductExistenceValidator(productRepository);
    }

    @Test
    void givenExistingUserId_whenValidate_thenNoViolations() {
        String id = "1";
        when(productRepository.existsById(id)).thenReturn(true);

        Set<SimpleViolation> violations = validator.validate(id);

        assertTrue(violations.isEmpty());
    }

    @Test
    void givenNonExistingUserId_whenValidate_thenReturnsViolation() {
        String id = "-55";
        when(productRepository.existsById(id)).thenReturn(false);

        Set<SimpleViolation> violations = validator.validate(id);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());

        SimpleViolation violation = null;
        for (SimpleViolation v : violations) {
            violation = v;
            break;
        }

        assertEquals("product.id", violation.getViolator());
        assertTrue(violation.getViolation().contains("does not exist"));
    }
}
