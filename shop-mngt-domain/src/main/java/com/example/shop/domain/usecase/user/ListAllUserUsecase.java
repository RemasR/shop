package com.example.shop.domain.usecase.user;

import com.example.shop.domain.model.User;
import com.example.shop.domain.repository.UserRepository;

import java.util.List;

public class ListAllUserUsecase {
    private final UserRepository userRepository;

    public ListAllUserUsecase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> execute() {
        return userRepository.findAllUsers();
    }
}