package com.example.shop.domain.usecase.user;

import com.example.shop.domain.repository.UserRepository;

import java.util.UUID;

public class DeleteUserUsecase {

    private final UserRepository userRepository;

    public DeleteUserUsecase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(UUID id) {
        checkIfNull(id);
        checkIfUserExists(id);

        userRepository.deleteById(id);
    }
}
