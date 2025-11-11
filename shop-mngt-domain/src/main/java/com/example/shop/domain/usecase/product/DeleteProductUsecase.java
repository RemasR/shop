package com.example.shop.domain.usecase.product;

import com.example.shop.domain.dto.SimpleViolation;
import com.example.shop.domain.entity.Product;
import com.example.shop.domain.repository.ProductRepository;
import com.example.shop.domain.validators.Validator;
import com.example.shop.domain.validators.product.ProductExistenceValidator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DeleteProductUsecase {
    private final ProductRepository productRepository;
    private final List<Validator<Integer>> productValidators;
    public DeleteProductUsecase(ProductRepository productRepository){
        this.productRepository = productRepository;
        this.productValidators = List.of(
                new ProductExistenceValidator(productRepository)
        );
    }
    public Product execute(Integer id) {
        Set<SimpleViolation> violations = new HashSet<>();
        for (Validator<Integer> validator : productValidators) {
            violations.addAll(validator.validate(id));
        }
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException("Validation failed: " + violations);
        }

        return productRepository.deleteById(id);
    }
}
