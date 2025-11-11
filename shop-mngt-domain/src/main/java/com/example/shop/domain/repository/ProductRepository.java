package com.example.shop.domain.repository;

import com.example.shop.domain.entity.Product;

import java.util.*;

public interface ProductRepository {

    Product save(Product product);

    Product findById(int id);

    List<Product> findAllProducts();

    Product deleteById(Integer id);

    boolean existsById(int id);
}