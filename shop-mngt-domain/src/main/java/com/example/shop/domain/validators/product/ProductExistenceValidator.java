package com.example.shop.domain.validators.product;

import com.example.shop.domain.dto.SimpleViolation;
import com.example.shop.domain.repository.ProductRepository;
import com.example.shop.domain.validators.Validator;

import java.util.HashSet;
import java.util.Set;

public class ProductExistenceValidator implements Validator<Integer> {

    private final ProductRepository productRepository;

    public ProductExistenceValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Set<SimpleViolation> validate(Integer id) {
        Set<SimpleViolation> violations = new HashSet<>();

        if (!productRepository.existsById(id)) {
            violations.add(new SimpleViolation("product.id", "Product with ID " + id + " does not exist"));
        }

        return violations;
    }
}
