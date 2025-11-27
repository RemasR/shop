package com.example.shop.repository.repoImpl;

import com.example.shop.domain.model.Order;
import com.example.shop.domain.model.OrderStatus;
import com.example.shop.domain.repository.OrderRepository;
import com.example.shop.entity.OrderEntity;
import com.example.shop.mapper.OrderMapper;
import com.example.shop.repository.jpa.SpringDataOrderRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@Primary
public class JpaOrderRepository implements OrderRepository {

    private final SpringDataOrderRepository springDataRepository;
    private final OrderMapper orderMapper;

    public JpaOrderRepository(SpringDataOrderRepository springDataRepository, OrderMapper orderMapper) {
        this.springDataRepository = springDataRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public Order save(Order order) {
        OrderEntity entity = orderMapper.toEntity(order);
        OrderEntity saved = springDataRepository.save(entity);
        return orderMapper.toModel(saved);
    }

    @Override
    public Order findById(String id) {
        OrderEntity entity = springDataRepository.findById(id).orElse(null);
        return orderMapper.toModel(entity);
    }

    @Override
    public List<Order> findAllOrders() {
        return springDataRepository.findAll().stream()
                .map(orderMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findByUserId(String userId) {
        return springDataRepository.findByUserId(userId).stream()
                .map(orderMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        return springDataRepository.findByStatus(status).stream()
                .map(orderMapper::toModel)
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