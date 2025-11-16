package com.example.shop.domain.usecase.product;

import com.example.shop.domain.repository.ProductRepository;
import com.example.shop.domain.usecase.ValidationExecutor;

public class DeleteProductUsecase {

    private final ProductRepository productRepository;
    private final ValidationExecutor<String> validationExecutor;

    public DeleteProductUsecase(ProductRepository productRepository, ValidationExecutor<String> validationExecutor) {
        this.productRepository = productRepository;
        this.validationExecutor = validationExecutor;
    }

    public void execute(String id) {
        validationExecutor.validateAndThrow(id);
        productRepository.deleteById(id);
    }
}
