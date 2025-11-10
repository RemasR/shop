package com.example.shop.domain.usecase.product;

import com.example.shop.domain.entity.Product;
import com.example.shop.domain.repository.ProductRepository;

public class DeleteProductUsecase {
    private final ProductRepository productRepository;
    public DeleteProductUsecase(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    public void execute(int id){
        productRepository.deleteById(id);
    }
}
