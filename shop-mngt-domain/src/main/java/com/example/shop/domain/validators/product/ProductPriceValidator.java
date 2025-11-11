package com.example.shop.domain.validators.product;

import com.example.shop.domain.dto.SimpleViolation;
import com.example.shop.domain.entity.Product;
import com.example.shop.domain.validators.Validator;

import java.util.HashSet;
import java.util.Set;

public class ProductPriceValidator implements Validator<Product> {
    @Override
    public Set<SimpleViolation> validate(Product product) throws IllegalArgumentException {
        Set<SimpleViolation> violations = new HashSet<>();

        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        if (product.getPrice() < 0) {
            violations.add(new SimpleViolation("price", "Price cannot be negative"));
        } else if (product.getPrice() > 1_000_000) {
            violations.add(new SimpleViolation("price", "Price exceeds allowed maximum"));
        }

        return violations;
    }
}
