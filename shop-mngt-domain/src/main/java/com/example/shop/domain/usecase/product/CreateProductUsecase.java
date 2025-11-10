package com.example.shop.domain.usecase.product;

import com.example.shop.domain.dto.ProductDTO;
import com.example.shop.domain.entity.Product;
import com.example.shop.domain.repository.ProductRepository;

public class CreateProductUsecase {
    private final ProductRepository productRepository;

    public CreateProductUsecase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product execute(ProductDTO dto) {
        Product product = new Product(
                1,
                "t-shirt",
                "black t-shirt",
                5.5
        );
        return productRepository.save(product);
    }
}
