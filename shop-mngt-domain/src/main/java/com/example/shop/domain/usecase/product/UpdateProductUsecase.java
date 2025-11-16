package com.example.shop.domain.usecase.product;

import com.example.shop.domain.dto.ProductDTO;
import com.example.shop.domain.entity.Product;
import com.example.shop.domain.repository.ProductRepository;
import com.example.shop.domain.usecase.ValidationExecutor;

public class UpdateProductUsecase {

    private final ProductRepository productRepository;
    private final ValidationExecutor<String> existenceValidationExecutor;
    private final ValidationExecutor<Product> productValidationExecutor;

    public UpdateProductUsecase(ProductRepository productRepository,
                                ValidationExecutor<String> existenceValidationExecutor,
                                ValidationExecutor<Product> productValidationExecutor) {
        this.productRepository = productRepository;
        this.existenceValidationExecutor = existenceValidationExecutor;
        this.productValidationExecutor = productValidationExecutor;
    }

    public Product execute(String id, ProductDTO dto) {
        existenceValidationExecutor.validateAndThrow(id);

        Product existing = productRepository.findById(id);

        if (dto.getName() != null) existing.setName(dto.getName());
        if (dto.getDescription() != null) existing.setDescription(dto.getDescription());
        if (dto.getPrice() != null) existing.setPrice(dto.getPrice());

        productValidationExecutor.validateAndThrow(existing);

        return productRepository.save(existing);
    }
}
