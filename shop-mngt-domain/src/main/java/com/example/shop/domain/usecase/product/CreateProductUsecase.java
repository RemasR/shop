package com.example.shop.domain.usecase.product;

import com.example.shop.domain.dto.ProductDTO;
import com.example.shop.domain.dto.SimpleViolation;
import com.example.shop.domain.entity.Product;
import com.example.shop.domain.repository.ProductRepository;
import com.example.shop.domain.validators.Validator;
import com.example.shop.domain.validators.product.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CreateProductUsecase {

    private final ProductRepository productRepository;
    private final List<Validator<Product>> productValidators;

    public CreateProductUsecase(ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.productValidators = List.of(
                new ProductNameValidator(),
                new ProductPriceValidator()
        );
    }

    public Product execute(ProductDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Product data cannot be null");
        }
        Product product = Product.builder()
                .id(0)
                .name(dto.getName())
                .price(dto.getPrice())
                .description(dto.getDescription())
                .build();

        Set<SimpleViolation> violations = new HashSet<>();
        for (Validator<Product> validator : productValidators) {
            violations.addAll(validator.validate(product));
        }
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException("Validation failed: " + violations);
        }

        return productRepository.save(product);
    }
}
