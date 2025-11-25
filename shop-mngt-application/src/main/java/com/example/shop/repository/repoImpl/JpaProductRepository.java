package com.example.shop.repository;

import com.example.shop.domain.entity.Product;
import com.example.shop.domain.repository.ProductRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JpaProductRepository implements ProductRepository {

    private final Map<String, Product> productStore = new HashMap<>();

    @Override
    public Product save(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        productStore.put(product.getId(), product);
        return product;
    }

    @Override
    public Product findById(String id) {
        return productStore.get(id);
    }

    @Override
    public List<Product> findAllProducts() {
        return new ArrayList<>(productStore.values());
    }

    @Override
    public void deleteById(String id) {
        productStore.remove(id);
    }

    @Override
    public boolean existsById(String id) {
        return productStore.containsKey(id);
    }
}