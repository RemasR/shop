package com.example.shop.mapper;

import com.example.shop.domain.model.Order;
import com.example.shop.domain.model.OrderItem;
import com.example.shop.entity.OrderEntity;
import com.example.shop.entity.OrderItemEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderEntity toEntity(Order order);

    Order toModel(OrderEntity entity);

    OrderItemEntity toEntity(OrderItem orderItem);

    OrderItem toModel(OrderItemEntity entity);
}