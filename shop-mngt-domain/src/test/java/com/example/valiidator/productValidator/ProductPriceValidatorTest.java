package com.example.valiidator.productValidator;

import com.example.shop.domain.dto.SimpleViolation;
import com.example.shop.domain.entity.Product;
import com.example.shop.domain.validators.product.ProductPriceValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ProductPriceValidatorTest {

    private ProductPriceValidator validator;

    @BeforeEach
    void setUp() {
        validator = new ProductPriceValidator();
    }

    @Test
    void givenValidPrice_whenValidate_thenNoViolations() {
        Product product = Product.builder()
                .name("t-shirt")
                .price(10.0)
                .description("Black T-Shirt")
                .build();

        Set<SimpleViolation> violations = validator.validate(product);

        assertTrue(violations.isEmpty());
    }

    @Test
    void givenNegativePrice_whenValidate_thenReturnsViolation() {
        Product product = Product.builder()
                .name("t-shirt")
                .price(-5.0)
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

        assertEquals("product.price", violation.getViolator());
        assertTrue(violation.getViolation().contains("cannot be negative"));
    }

    @Test
    void givenZeroPrice_whenValidate_thenReturnsViolation() {
        Product product = Product.builder()
                .name("t-shirt")
                .price(0.0)
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

        assertEquals("product.price", violation.getViolator());
        assertTrue(violation.getViolation().contains("cannot be negative"));
    }

    @Test
    void givenPriceExceedingMaximum_whenValidate_thenReturnsViolation() {
        Product product = Product.builder()
                .name("t-shirt")
                .price(1_500_000.0)
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

        assertEquals("product.price", violation.getViolator());
        assertTrue(violation.getViolation().contains("exceeds allowed maximum"));
    }

    @Test
    void givenNullProduct_whenValidate_thenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            validator.validate(null);
        });
    }
}