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
    private void checkIfNull(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
    }

    private void checkIfUserExists(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User with ID " + id + " does not exist");
        }
    }
}
