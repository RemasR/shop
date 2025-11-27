package com.example.shop.mapper;

import com.example.shop.domain.model.User;
import com.example.shop.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserEntity toEntity(User user);

    User toModel(UserEntity entity);
}