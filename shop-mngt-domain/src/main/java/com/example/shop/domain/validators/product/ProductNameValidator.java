package com.example.shop.domain.validators.product;

import com.example.shop.domain.dto.SimpleViolation;
import com.example.shop.domain.entity.Product;
import com.example.shop.domain.validators.Validator;

import java.util.HashSet;
import java.util.Set;

public class ProductNameValidator implements Validator<Product> {
    public Set<SimpleViolation> validate(Product product) {
        Set<SimpleViolation> violations = new HashSet<>();
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            violations.add(new SimpleViolation("product.name", "Name cannot be null or empty"));
        } else if (product.getName().length() < 3) {
            violations.add(new SimpleViolation("product.name", "Name must be at least 3 characters"));
        }
        return violations;
    }
}


