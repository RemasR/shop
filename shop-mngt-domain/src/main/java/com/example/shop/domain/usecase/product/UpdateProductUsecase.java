package com.example.shop.domain.usecase.product;

import com.example.shop.domain.dto.ProductDTO;
import com.example.shop.domain.entity.Product;
import com.example.shop.domain.repository.ProductRepository;

public class UpdateProductUsecase {
    private final ProductRepository productRepository;
    public UpdateProductUsecase(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    public Product execute(int id, ProductDTO dto){
        if(dto == null){
            throw new IllegalArgumentException("Update data cannot be null");
        }
        Product existingProduct = productRepository.findById(id);

        if(dto.getName() != null){
            existingProduct.setName(dto.getName());
        }

        if(dto.getDescription() != null){
            existingProduct.setDescription(dto.getDescription());
        }

        if(dto.getPrice() != null){
            existingProduct.setPrice(dto.getPrice());
        }
        return productRepository.save(existingProduct);
    }
}
