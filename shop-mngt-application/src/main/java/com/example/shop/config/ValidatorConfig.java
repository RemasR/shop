package com.example.shop.config;

import com.example.shop.domain.model.Order;
import com.example.shop.domain.model.Product;
import com.example.shop.domain.model.User;
import com.example.shop.domain.repository.OrderRepository;
import com.example.shop.domain.repository.ProductRepository;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.usecase.ValidationExecutor;
import com.example.shop.domain.validators.Validator;
import com.example.shop.domain.validators.order.ItemsPresenceValidator;
import com.example.shop.domain.validators.order.OrderExistenceValidator;
import com.example.shop.domain.validators.product.ProductExistenceValidator;
import com.example.shop.domain.validators.product.ProductNameValidator;
import com.example.shop.domain.validators.product.ProductPriceValidator;
import com.example.shop.domain.validators.user.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ValidatorConfig {

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
    public ValidationExecutor<Product> productValidationExecutor() {
        List<Validator<Product>> validators = List.of(
                new ProductNameValidator(),
                new ProductPriceValidator()
        );
        return new ValidationExecutor<>(validators);
    }

    @Bean
    public ValidationExecutor<String> productExistenceValidationExecutor(ProductRepository productRepository) {
        List<Validator<String>> validators = List.of(
                new ProductExistenceValidator(productRepository)
        );
        return new ValidationExecutor<>(validators);
    }

    @Bean
    public ValidationExecutor<Order> orderItemPresenceValidatorExecutor(UserRepository userRepository,
                                                                ProductRepository productRepository) {
        List<Validator<Order>> validators = List.of(
                new ItemsPresenceValidator(userRepository, productRepository)
        );
        return new ValidationExecutor<>(validators);
    }

    @Bean
    public ValidationExecutor<String> orderExistenceValidationExecutor(OrderRepository orderRepository) {
        List<Validator<String>> validators = List.of(
                new OrderExistenceValidator(orderRepository)
        );
        return new ValidationExecutor<>(validators);
    }
}
