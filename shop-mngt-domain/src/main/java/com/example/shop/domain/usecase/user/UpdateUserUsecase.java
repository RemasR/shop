package com.example.shop.domain.usecase.user;

import com.example.shop.domain.dto.UserDTO;
import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.usecase.ValidationExecutor;
import com.example.shop.domain.validators.user.*;

public class UpdateUserUsecase {

    private final UserRepository userRepository;
    private final ValidationExecutor<String> existenceValidationExecutor;
    private final ValidationExecutor<User> userValidationExecutor;

    public UpdateUserUsecase(UserRepository userRepository,
                             ValidationExecutor<String> existenceValidationExecutor,
                             ValidationExecutor<User> userValidationExecutor) {
        this.userRepository = userRepository;
        this.existenceValidationExecutor = existenceValidationExecutor;
        this.userValidationExecutor = userValidationExecutor;
    }
    //string id
    public User execute(String userId, UserDTO dto) {
        existenceValidationExecutor.validateAndThrow(userId);

        User existingUser = userRepository.findById(userId);

        if (dto.getName() != null) existingUser.setName(dto.getName());
        if (dto.getEmail() != null) existingUser.setEmail(dto.getEmail());
        if (dto.getPhoneNumber() != null) existingUser.setPhoneNumber(dto.getPhoneNumber());

        userValidationExecutor.validateAndThrow(existingUser);

        return userRepository.save(existingUser);
    }
}

