package com.example.shop.mapper;

import com.example.shop.domain.model.Product;
import com.example.shop.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductEntity toEntity(Product product);

    Product toModel(ProductEntity entity);
}