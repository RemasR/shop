package com.example.shop.domain.usecase.product;

import com.example.shop.domain.entity.Product;
import com.example.shop.domain.repository.ProductRepository;
import com.example.shop.domain.usecase.ValidationExecutor;

public class FindProductByIdUsecase {

    private final ProductRepository productRepository;
    private final ValidationExecutor<Integer> validationExecutor;

    public FindProductByIdUsecase(ProductRepository productRepository, ValidationExecutor<Integer> validationExecutor) {
        this.productRepository = productRepository;
        this.validationExecutor = validationExecutor;
    }

    public Product execute(Integer id) {
        validationExecutor.validateAndThrow(id);
        return productRepository.findById(id);
    }
}
