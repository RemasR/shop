package com.example.shop.repository.jpa;

import com.example.shop.domain.model.Order;
import com.example.shop.domain.model.OrderStatus;
import com.example.shop.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataOrderRepository extends JpaRepository<OrderEntity, String> {
    List<OrderEntity> findByUserId(String userId);
    List<OrderEntity> findByStatus(OrderStatus status);
}
