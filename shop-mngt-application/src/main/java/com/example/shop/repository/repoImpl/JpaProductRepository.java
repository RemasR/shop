package com.example.shop.repository.repoImpl;

import com.example.shop.domain.entity.Product;
import com.example.shop.domain.repository.ProductRepository;
import com.example.shop.repository.jpa.SpringDataProductRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
public class JpaProductRepository implements ProductRepository {
    private SpringDataProductRepository springDataRepository;

    public JpaProductRepository(SpringDataProductRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Product save(Product product) {
        return springDataRepository.save(product);
    }

    @Override
    public Product findById(String id) {
        return springDataRepository.findById(id).orElse(null);
    }

    @Override
    public List<Product> findAllProducts() {
        return springDataRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
        springDataRepository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
      return springDataRepository.existsById(id);
    }
}