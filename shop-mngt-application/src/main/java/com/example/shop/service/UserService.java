package com.example.shop.service;

import com.example.shop.domain.dto.UserDTO;
import com.example.shop.domain.entity.User;
import com.example.shop.domain.usecase.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final CreateUserUsecase createUserUsecase;
    private final UpdateUserUsecase updateUserUsecase;
    private final DeleteUserUsecase deleteUserUsecase;
    private final FindUserByIdUsecase findUserByIdUsecase;
    private final ListAllUserUsecase listAllUserUsecase;

    public User registerUser(UserDTO dto){
        return createUserUsecase.execute(dto);
    }
    public User getUserById(String id){
        return findUserByIdUsecase.execute(id);
    }
    public User updateUser(String id, UserDTO dto){
        return updateUserUsecase.execute(id, dto);
    }
    public void deleteUser(String id){
        deleteUserUsecase.execute(id);
    }
    public List<User> getAllUsers(){
        return listAllUserUsecase.execute();
    }
}