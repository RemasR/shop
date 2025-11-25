package com.example.shop.repository.repoImpl;

import com.example.shop.domain.entity.Order;
import com.example.shop.domain.entity.OrderStatus;
import com.example.shop.domain.repository.OrderRepository;
import com.example.shop.repository.jpa.SpringDataOrderRepository;
import com.example.shop.repository.jpa.SpringDataProductRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Primary
public class JpaOrderRepository implements OrderRepository {
    private SpringDataOrderRepository springDataRepository;

    public JpaOrderRepository(SpringDataOrderRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Order save(Order order) {
       return springDataRepository.save(order);
    }

    @Override
    public Order findById(String id) {
        return springDataRepository.findById(id).orElse(null);
    }

    @Override
    public List<Order> findAllOrders() {
        return springDataRepository.findAll();
    }

    @Override
    public List<Order> findByUserId(String userId) {
        return springDataRepository.findByUserId(userId);
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
       return springDataRepository.findByStatus(status);
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