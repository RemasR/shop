package com.example.shop.config;

import com.example.shop.domain.usecase.order.*;
import com.example.shop.domain.usecase.product.*;
import com.example.shop.domain.usecase.user.*;
import com.example.shop.service.OrderService;
import com.example.shop.service.ProductService;
import com.example.shop.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {
    @Bean
    public UserService userService(CreateUserUsecase createUserUsecase,
                                   UpdateUserUsecase updateUserUsecase,
                                   DeleteUserUsecase deleteUserUsecase,
                                   FindUserByIdUsecase findUserByIdUsecase,
                                   ListAllUserUsecase listAllUserUsecase) {
        return new UserService(createUserUsecase, updateUserUsecase, deleteUserUsecase,
                findUserByIdUsecase, listAllUserUsecase);
    }

    @Bean
    public ProductService productService(CreateProductUsecase createProductUsecase,
                                         UpdateProductUsecase updateProductUsecase,
                                         DeleteProductUsecase deleteProductUsecase,
                                         FindProductByIdUsecase findProductByIdUsecase,
                                         ListAllProductUsecase listAllProductUsecase) {
        return new ProductService(createProductUsecase, updateProductUsecase, deleteProductUsecase,
                findProductByIdUsecase, listAllProductUsecase);
    }

    @Bean
    public OrderService orderService(CreateOrderUsecase createOrderUsecase,
                                     UpdateOrderUsecase updateOrderUsecase,
                                     DeleteOrderUsecase deleteOrderUsecase,
                                     FindOrderByIdUsecase findOrderByIdUsecase,
                                     FindOrderByStatusUsecase findOrderByStatusUsecase,
                                     FindOrderByUserUsecase findOrderByUserUsecase) {
        return new OrderService(createOrderUsecase, updateOrderUsecase, deleteOrderUsecase,
                findOrderByIdUsecase, findOrderByStatusUsecase, findOrderByUserUsecase);
    }
}
