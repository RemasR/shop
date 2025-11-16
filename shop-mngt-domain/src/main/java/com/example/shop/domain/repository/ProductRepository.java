package com.example.shop.domain.repository;

import com.example.shop.domain.entity.Product;

import java.util.*;

public interface ProductRepository {

    Product save(Product product);

    Product findById(String id);

    List<Product> findAllProducts();

    Product deleteById(String id);

    boolean existsById(String id);
}