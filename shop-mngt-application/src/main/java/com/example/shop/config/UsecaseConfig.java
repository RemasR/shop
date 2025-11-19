package com.example.shop.config;

import com.example.shop.domain.dto.OrderDTO;
import com.example.shop.domain.dto.ProductDTO;
import com.example.shop.domain.dto.UserDTO;
import com.example.shop.domain.entity.Product;
import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.OrderRepository;
import com.example.shop.domain.repository.ProductRepository;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.usecase.ValidationExecutor;
import com.example.shop.domain.usecase.order.*;
import com.example.shop.domain.usecase.product.*;
import com.example.shop.domain.usecase.user.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UsecaseConfig {

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
    public CreateProductUsecase createProductUsecase(ProductRepository productRepository,
                                                     ValidationExecutor<ProductDTO> productDTOValidationExecutor) {
        return new CreateProductUsecase(productRepository, productDTOValidationExecutor);
    }

    @Bean
    public UpdateProductUsecase updateProductUsecase(ProductRepository productRepository,
                                                     ValidationExecutor<String> productExistenceValidationExecutor,
                                                     ValidationExecutor<Product> productValidationExecutor) {
        return new UpdateProductUsecase(productRepository, productExistenceValidationExecutor, productValidationExecutor);
    }

    @Bean
    public DeleteProductUsecase deleteProductUsecase(ProductRepository productRepository,
                                                     ValidationExecutor<String> productExistenceValidationExecutor) {
        return new DeleteProductUsecase(productRepository, productExistenceValidationExecutor);
    }

    @Bean
    public FindProductByIdUsecase findProductByIdUsecase(ProductRepository productRepository,
                                                         ValidationExecutor<String> productExistenceValidationExecutor) {
        return new FindProductByIdUsecase(productRepository, productExistenceValidationExecutor);
    }

    @Bean
    public ListAllProductUsecase listAllProductUsecase(ProductRepository productRepository) {
        return new ListAllProductUsecase(productRepository);
    }

    @Bean
    public CreateOrderUsecase createOrderUsecase(OrderRepository orderRepository,
                                                 ProductRepository productRepository,
                                                 ValidationExecutor<OrderDTO> orderDTOValidationExecutor) {
        return new CreateOrderUsecase(orderRepository, productRepository, orderDTOValidationExecutor);
    }

    @Bean
    public UpdateOrderUsecase updateOrderUsecase(OrderRepository orderRepository,
                                                 ProductRepository productRepository,
                                                 ValidationExecutor<String> orderExistenceValidationExecutor,
                                                 ValidationExecutor<OrderDTO> orderDTOValidationExecutor) {
        return new UpdateOrderUsecase(orderRepository, productRepository,
                orderExistenceValidationExecutor,
                orderDTOValidationExecutor);
    }

    @Bean
    public DeleteOrderUsecase deleteOrderUsecase(OrderRepository orderRepository,
                                                 ValidationExecutor<String> orderExistenceValidationExecutor) {
        return new DeleteOrderUsecase(orderRepository, orderExistenceValidationExecutor);
    }

    @Bean
    public FindOrderByIdUsecase findOrderByIdUsecase(OrderRepository orderRepository,
                                                     ValidationExecutor<String> orderExistenceValidationExecutor) {
        return new FindOrderByIdUsecase(orderRepository, orderExistenceValidationExecutor);
    }

    @Bean
    public FindOrderByStatusUsecase findOrderByStatusUsecase(OrderRepository orderRepository) {
        return new FindOrderByStatusUsecase(orderRepository);
    }

    @Bean
    public FindOrderByUserUsecase findOrderByUserUsecase(OrderRepository orderRepository,
                                                         ValidationExecutor<String> userExistenceValidationExecutor) {
        return new FindOrderByUserUsecase(orderRepository, userExistenceValidationExecutor);
    }

    @Bean
    public ListAllOrderUsecase listAllOrderUsecase(OrderRepository orderRepository) {
        return new ListAllOrderUsecase(orderRepository);
    }
}
