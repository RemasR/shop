package com.example.shop.service;

import com.example.shop.domain.dto.ProductDTO;
import com.example.shop.domain.entity.Product;
import com.example.shop.domain.usecase.product.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final CreateProductUsecase createProductUsecase;
    private final UpdateProductUsecase updateProductUsecase;
    private final DeleteProductUsecase deleteProductUsecase;
    private final FindProductByIdUsecase findProductByIdUsecase;
    private final ListAllProductUsecase listAllProductUsecase;

    public Product createProduct(ProductDTO dto){
        return createProductUsecase.execute(dto);
    }
    public Product getProductById(String id){
        return findProductByIdUsecase.execute(id);
    }
    public Product updateProduct(String id, ProductDTO dto){
        return updateProductUsecase.execute(id, dto);
    }
    public void deleteProduct(String id){
        deleteProductUsecase.execute(id);
    }
    public List<Product> getAllProducts(){
        return listAllProductUsecase.execute();
    }
}