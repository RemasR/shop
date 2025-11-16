package com.example.shop.domain.usecase.user;

import com.example.shop.domain.dto.UserDTO;
import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.usecase.ValidationExecutor;
import com.example.shop.domain.validators.user.*;

import java.util.UUID;

public class UpdateUserUsecase {

    private final UserRepository userRepository;
    private final ValidationExecutor<UUID> existenceValidationExecutor;
    private final ValidationExecutor<User> userValidationExecutor;

    public UpdateUserUsecase(UserRepository userRepository,
                             ValidationExecutor<UUID> existenceValidationExecutor,
                             ValidationExecutor<User> userValidationExecutor) {
        this.userRepository = userRepository;
        this.existenceValidationExecutor = existenceValidationExecutor;
        this.userValidationExecutor = userValidationExecutor;
    }

    public User execute(UUID userId, UserDTO dto) {
        existenceValidationExecutor.validateAndThrow(userId);

        User existingUser = userRepository.findById(userId);

        if (dto.getName() != null) existingUser.setName(dto.getName());
        if (dto.getEmail() != null) existingUser.setEmail(dto.getEmail());
        if (dto.getPhoneNumber() != null) existingUser.setPhoneNumber(dto.getPhoneNumber());

        userValidationExecutor.validateAndThrow(existingUser);

        return userRepository.save(existingUser);
    }
}

