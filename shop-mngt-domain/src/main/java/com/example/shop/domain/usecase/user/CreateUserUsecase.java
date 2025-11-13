package com.example.shop.domain.usecase.user;

import com.example.shop.domain.dto.UserDTO;
import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.usecase.ValidationExecutor;
import com.example.shop.domain.validators.user.*;

import java.util.UUID;
    public class CreateUserUsecase {
        private final UserRepository userRepository;
        private final ValidationExecutor<UserDTO> validationExecutor;

        public CreateUserUsecase(UserRepository userRepository, ValidationExecutor<UserDTO> validationExecutor) {
            this.userRepository = userRepository;
            this.validationExecutor = validationExecutor;
        }

        public User execute(UserDTO dto) {
            validationExecutor.validateAndThrow(dto);

            User user = new User(
                    UUID.randomUUID(),
                    dto.getName(),
                    dto.getEmail(),
                    dto.getPhoneNumber()
            );

            return userRepository.save(user);
        }
}
