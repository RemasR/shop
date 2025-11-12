package com.example.shop.domain.usecase.user;

import com.example.shop.domain.dto.UserDTO;
import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.usecase.ValidationExecutor;
import com.example.shop.domain.validators.user.*;

import java.util.List;
import java.util.UUID;

public class UpdateUserUsecase {

    private final UserRepository userRepository;
    private final ValidationExecutor<User> userValidationExecutor;
    private final ValidationExecutor<UUID> idValidationExecutor;

    public UpdateUserUsecase(UserRepository userRepository) {
        this.userRepository = userRepository;

        this.userValidationExecutor = new ValidationExecutor<>(
                List.of(
                        new UsernameValidator(),
                        new EmailValidator(),
                        new PhonenumberValidator(),
                        new EmailUniquenessValidator(userRepository)
                )
        );

        this.idValidationExecutor = new ValidationExecutor<>(
                List.of(
                        new UserIdValidator(),
                        new UserExistenceValidator(userRepository)
                )
        );
    }

    public User execute(UUID userId, UserDTO dto) {
        idValidationExecutor.validateAndThrow(userId);

        if (dto == null) {
            throw new IllegalArgumentException("Update data cannot be null");
        }

        User existingUser = userRepository.findById(userId);

        if (dto.getName() != null) existingUser.setName(dto.getName());
        if (dto.getEmail() != null) existingUser.setEmail(dto.getEmail());
        if (dto.getPhoneNumber() != null) existingUser.setPhoneNumber(dto.getPhoneNumber());

        userValidationExecutor.validateAndThrow(existingUser);

        return userRepository.save(existingUser);
    }
}
