package com.example.shop.domain.usecase.product;

import com.example.shop.domain.dto.ProductDTO;
import com.example.shop.domain.model.Product;
import com.example.shop.domain.repository.ProductRepository;
import com.example.shop.domain.usecase.ValidationExecutor;

import java.util.UUID;

public class CreateProductUsecase {

    private final ProductRepository productRepository;
    private final ValidationExecutor<Product> validationExecutor;

    public CreateProductUsecase(ProductRepository productRepository, ValidationExecutor<Product> validationExecutor) {
        this.productRepository = productRepository;
        this.validationExecutor = validationExecutor;
    }

    public Product execute(ProductDTO dto) {


        Product product = Product.builder()
                .id(UUID.randomUUID().toString())
                .name(dto.getName())
                .price(dto.getPrice())
                .description(dto.getDescription())
                .build();

        validationExecutor.validateAndThrow(product);
        return productRepository.save(product);
    }
}
