package com.example.shop.repository.repoImpl;

import com.example.shop.domain.model.Product;
import com.example.shop.domain.repository.ProductRepository;
import com.example.shop.entity.ProductEntity;
import com.example.shop.mapper.ProductMapper;
import com.example.shop.repository.jpa.SpringDataProductRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@Primary
public class JpaProductRepository implements ProductRepository {
    private final SpringDataProductRepository springDataRepository;
    private final ProductMapper productMapper;

    public JpaProductRepository(SpringDataProductRepository springDataRepository, ProductMapper productMapper) {
        this.springDataRepository = springDataRepository;
        this.productMapper = productMapper;
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity = productMapper.toEntity(product);
        ProductEntity saved = springDataRepository.save(entity);
        return productMapper.toModel(saved);
    }

    @Override
    public Product findById(String id) {
        ProductEntity entity = springDataRepository.findById(id).orElse(null);
        return productMapper.toModel(entity);
    }

    @Override
    public List<Product> findAllProducts() {
        return springDataRepository.findAll().stream()
                .map(productMapper::toModel)
                .collect(Collectors.toList());
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