package com.example.shop.config;

import com.example.shop.domain.repository.OrderRepository;
import com.example.shop.domain.repository.ProductRepository;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.repository.MemoryOrderRepository;
import com.example.shop.repository.MemoryProductRepository;
import com.example.shop.repository.MemoryUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {
    @Bean
    public UserRepository userRepository() {
        return new MemoryUserRepository();
    }

    @Bean
    public ProductRepository productRepository() {
        return new MemoryProductRepository();
    }

    @Bean
    public OrderRepository orderRepository() {
        return new MemoryOrderRepository();
    }
}
