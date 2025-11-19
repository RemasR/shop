package com.example.shop.domain.repository;

import com.example.shop.domain.entity.Product;

import java.util.List;

public interface ProductRepository {

    Product save(Product product);

    Product findById(String id);

    List<Product> findAllProducts();

    void deleteById(String id);

    boolean existsById(String id);
}