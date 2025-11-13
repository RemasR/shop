package com.example.productValidator;

import com.example.shop.domain.dto.SimpleViolation;
import com.example.shop.domain.entity.Product;
import com.example.shop.domain.entity.User;
import com.example.shop.domain.validators.product.ProductNameValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductNameValidatorTest {

    private ProductNameValidator validator;

    @BeforeEach
    void setUp() {
        validator = new ProductNameValidator();
    }

    @Test
    void givenValidName_whenValidate_thenNoViolations() {
    Product product = Product.builder()
            .name("t-shirt")
            .price(5.0)
            .description("Black T-Shirt")
            .build();

        Set<SimpleViolation> violations = validator.validate(product);

        assertTrue(violations.isEmpty());
    }

    @Test
    void givenNullName_whenValidate_thenReturnsViolation() {
        Product product = Product.builder()
                .price(5.0)
                .description("Black T-Shirt")
                .build();

        Set<SimpleViolation> violations = validator.validate(product);

        assertFalse(violations.isEmpty());

        assertEquals(1, violations.size());

        SimpleViolation violation = null;
        for (SimpleViolation v : violations) {
            violation = v;
            break;
        }

        assertEquals("product.name", violation.getViolator());
        assertTrue(violation.getViolation().contains("cannot be null or empty"));
    }
}
