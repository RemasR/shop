package com.example.shop.repository;

import com.example.shop.domain.entity.Product;
import com.example.shop.domain.repository.ProductRepository;

import java.util.*;

public class ProductRepositoryImpl implements ProductRepository {

    private final Map<Integer, Product> productStore = new HashMap<>();

    @Override
    public Product save(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        productStore.put(product.getId(), product);
        return product;
    }

    @Override
    public Product findById(int id) {
        return productStore.get(id);
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(productStore.values());
    }

    @Override
    public void deleteById(int id) {
        productStore.remove(id);
    }

    @Override
    public boolean existsById(int id) {
        return productStore.containsKey(id);
    }
}