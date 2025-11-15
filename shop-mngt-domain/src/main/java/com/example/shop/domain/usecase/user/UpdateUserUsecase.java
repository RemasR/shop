package com.example.shop.domain.usecase.user;

import com.example.shop.domain.dto.UserDTO;
import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.usecase.ValidationExecutor;
import com.example.shop.domain.validators.user.*;

import java.util.UUID;

public class UpdateUserUsecase {

    private final UserRepository userRepository;
    private final ValidationExecutor<UserDTO> validationExecutor;

    public UpdateUserUsecase(UserRepository userRepository, ValidationExecutor<UserDTO> validationExecutor) {
        this.userRepository = userRepository;
        this.validationExecutor = validationExecutor;
    }

    public User execute(UUID userId, UserDTO dto) {
        validationExecutor.validateAndThrow(dto);

        if (!userRepository.existsById(userId)) { // this should be done in a validator, fix it later
            throw new RuntimeException("User not found");
        }

        User existingUser = userRepository.findById(userId);

        if (dto.getName() != null) existingUser.setName(dto.getName());
        if (dto.getEmail() != null) existingUser.setEmail(dto.getEmail());
        if (dto.getPhoneNumber() != null) existingUser.setPhoneNumber(dto.getPhoneNumber());

        return userRepository.save(existingUser);
    }
}

