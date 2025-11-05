package com.example.shop.service;

import com.example.shop.domain.entity.User;
import com.example.shop.domain.usecase.user.*;
import com.example.shop.dto.UserDTO;

import java.util.UUID;

public class UserService {
    private final CreateUserUsecase createUserUsecase;
    private final DeleteUserUsecase deleteUserUsecase;

    public UserService(CreateUserUsecase createUserUsecase,
                       DeleteUserUsecase deleteUserUsecase) {
        this.createUserUsecase = createUserUsecase;
        this.deleteUserUsecase = deleteUserUsecase;
    }

    public User createUser(UserDTO dto) {
        return createUserUsecase.execute(dto);
    }

    public void deleteUser(UUID id) {
        deleteUserUsecase.execute(id);
    }
}

