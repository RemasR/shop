package com.example.shop.domain.usecase.user;

import com.example.shop.domain.dto.UserDTO;
import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.usecase.ValidationExecutor;
import com.example.shop.domain.validators.user.*;

import java.util.List;
import java.util.UUID;

public class CreateUserUsecase {

    private final UserRepository userRepository;
    private final ValidationExecutor<User> validationExecutor;

    public CreateUserUsecase(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.validationExecutor = new ValidationExecutor<>(
                List.of(
                        new UsernameValidator(),
                        new EmailValidator(userRepository),
                        new PhonenumberValidator()
        ));
    }

    public User execute(UserDTO dto) {
        User user = new User(
                UUID.randomUUID(),
                dto.getName(),
                dto.getEmail(),
                dto.getPhoneNumber()
        );
        validationExecutor.validateAndThrow(user);

        return userRepository.save(user);
    }
}
