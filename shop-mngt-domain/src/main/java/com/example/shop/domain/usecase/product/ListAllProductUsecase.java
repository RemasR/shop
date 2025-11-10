package com.example.shop.domain.usecase.product;

import com.example.shop.domain.entity.Product;
import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.ProductRepository;

import java.util.List;

public class ListAllProductUsecase {
    private final ProductRepository productRepository;

    public ListAllProductUsecase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> execute() {
        return productRepository.findAll();
    }
}
