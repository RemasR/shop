package com.example.shop.config;

import com.example.shop.domain.dto.UserDTO;
import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.usecase.ValidationExecutor;
import com.example.shop.domain.usecase.user.*;
import com.example.shop.domain.validators.Validator;
import com.example.shop.domain.validators.user.*;
import com.example.shop.repository.MemoryUserRepository;
import com.example.shop.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AppConfig {
    //should i have seperate config file for each class?
    //UserConfig ProductConfig OrderConfig
    @Bean
    public UserRepository userRepository() {
        return new MemoryUserRepository();
    }

    @Bean
    public ValidationExecutor<UserDTO> userDTOValidationExecutor() {
        List<Validator<UserDTO>> validators = List.of();
        return new ValidationExecutor<>(validators);
    }

    @Bean
    public ValidationExecutor<User> userValidationExecutor(UserRepository userRepository) {
        List<Validator<User>> validators = List.of(
                new UsernameValidator(),
                new EmailValidator(userRepository),
                new PhonenumberValidator()
        );
        return new ValidationExecutor<>(validators);
    }

    @Bean
    public ValidationExecutor<String> userExistenceValidationExecutor(UserRepository userRepository) {
        List<Validator<String>> validators = List.of(
                new UserIdValidator(),
                new UserExistenceValidator(userRepository)
        );
        return new ValidationExecutor<>(validators);
    }

    @Bean
    public CreateUserUsecase createUserUsecase(UserRepository userRepository,
                                               ValidationExecutor<UserDTO> userDTOValidationExecutor) {
        return new CreateUserUsecase(userRepository, userDTOValidationExecutor);
    }

    @Bean
    public UpdateUserUsecase updateUserUsecase(UserRepository userRepository,
                                               ValidationExecutor<String> userExistenceValidationExecutor,
                                               ValidationExecutor<User> userValidationExecutor) {
        return new UpdateUserUsecase(userRepository, userExistenceValidationExecutor, userValidationExecutor);
    }

    @Bean
    public DeleteUserUsecase deleteUserUsecase(UserRepository userRepository,
                                               ValidationExecutor<String> userExistenceValidationExecutor) {
        return new DeleteUserUsecase(userRepository, userExistenceValidationExecutor);
    }

    @Bean
    public FindUserByIdUsecase findUserByIdUsecase(UserRepository userRepository,
                                                   ValidationExecutor<String> userExistenceValidationExecutor) {
        return new FindUserByIdUsecase(userRepository, userExistenceValidationExecutor);
    }

    @Bean
    public ListAllUserUsecase listAllUserUsecase(UserRepository userRepository) {
        return new ListAllUserUsecase(userRepository);
    }
    @Bean
    public UserService userService(CreateUserUsecase createUserUsecase,
                                   UpdateUserUsecase updateUserUsecase,
                                   DeleteUserUsecase deleteUserUsecase,
                                   FindUserByIdUsecase findUserByIdUsecase,
                                   ListAllUserUsecase listAllUserUsecase) {
        return new UserService(createUserUsecase, updateUserUsecase, deleteUserUsecase,
                findUserByIdUsecase, listAllUserUsecase);
    }
}