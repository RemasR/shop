package com.example.shop.domain.usecase.user;

import com.example.shop.domain.dto.UserDTO;
import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.usecase.ValidationExecutor;
import com.example.shop.domain.validators.Validator;
import com.example.shop.domain.validators.user.*;

import java.util.List;
import java.util.UUID;

public class CreateUserUsecase {

    private final UserRepository userRepository;
    private final ValidationExecutor<User> validationExecutor;
    private final List<Validator<User>> userValidators;

    public CreateUserUsecase(UserRepository userRepository, ValidationExecutor<User> validationExecutor) {
        this.userRepository = userRepository;
        this.validationExecutor = validationExecutor;
        this.userValidators = List.of(
                new UsernameValidator(),
                new EmailValidator(),
                new PhonenumberValidator()
        );
    }

    public User execute(UserDTO dto) {
        User user = new User(
                UUID.randomUUID(),
                dto.getName(),
                dto.getEmail(),
                dto.getPhoneNumber()
        );
        validationExecutor.validate(user, userValidators);

        return userRepository.save(user);
    }
}
