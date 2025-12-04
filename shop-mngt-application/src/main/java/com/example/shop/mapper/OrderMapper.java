package com.example.shop.mapper;

import com.example.shop.domain.model.Order;
import com.example.shop.domain.model.OrderItem;
import com.example.shop.entity.ItemEntity;
import com.example.shop.entity.OrderEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderEntity toEntity(Order order);

    Order toModel(OrderEntity entity);

    ItemEntity toEntity(OrderItem orderItem);

    OrderItem toModel(ItemEntity entity);
}