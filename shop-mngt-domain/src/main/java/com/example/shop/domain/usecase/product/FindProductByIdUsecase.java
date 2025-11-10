package com.example.shop.domain.usecase.product;

import com.example.shop.domain.entity.Product;
import com.example.shop.domain.repository.ProductRepository;

public class FindProductByIdUsecase {
    private final ProductRepository productRepository;

    public FindProductByIdUsecase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
        public Product execute(int id){

        return productRepository.findById(id);
        }
}