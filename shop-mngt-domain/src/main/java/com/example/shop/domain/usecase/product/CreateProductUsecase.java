package com.example.shop.domain.usecase.product;

import com.example.shop.domain.dto.ProductDTO;
import com.example.shop.domain.entity.Product;
import com.example.shop.domain.repository.ProductRepository;
import com.example.shop.domain.usecase.ValidationExecutor;

public class CreateProductUsecase {

    private final ProductRepository productRepository;
    private final ValidationExecutor<ProductDTO> validationExecutor;

    public CreateProductUsecase(ProductRepository productRepository, ValidationExecutor<ProductDTO> validationExecutor) {
        this.productRepository = productRepository;
        this.validationExecutor = validationExecutor;
    }

    public Product execute(ProductDTO dto) {
        validationExecutor.validateAndThrow(dto);

        Product product = Product.builder() //Maybe we should have a constructor that takes a dto and created a product
                .id(0)
                .name(dto.getName())
                .price(dto.getPrice())
                .description(dto.getDescription())
                .build();

        return productRepository.save(product);
    }
}
