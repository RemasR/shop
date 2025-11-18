package com.example.usecase.order;

import com.example.shop.domain.dto.OrderDTO;
import com.example.shop.domain.dto.OrderItemDTO;
import com.example.shop.domain.entity.Order;
import com.example.shop.domain.entity.OrderStatus;
import com.example.shop.domain.entity.Product;
import com.example.shop.domain.entity.User;
import com.example.shop.domain.repository.OrderRepository;
import com.example.shop.domain.repository.ProductRepository;
import com.example.shop.domain.repository.UserRepository;
import com.example.shop.domain.usecase.ValidationException;
import com.example.shop.domain.usecase.ValidationExecutor;
import com.example.shop.domain.usecase.order.CreateOrderUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CreateOrderUsecaseTest {

    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private ValidationExecutor<OrderDTO> validationExecutor;
    private CreateOrderUsecase createOrderUsecase;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        userRepository = mock(UserRepository.class);
        productRepository = mock(ProductRepository.class);
        validationExecutor = mock(ValidationExecutor.class);
        createOrderUsecase = new CreateOrderUsecase(orderRepository, userRepository, productRepository, validationExecutor);
    }

    @Test
    void givenValidOrderDTO_whenExecute_thenOrderIsCreated() {
        String userId = UUID.randomUUID().toString();
        User user = new User(userId, "Raslan", "raslan@test.com", "+962791234567");

        Product product = Product.builder()
                .id("1")
                .name("Laptop")
                .price(1000.0)
                .description("Gaming laptop")
                .build();

        OrderItemDTO itemDTO = new OrderItemDTO("1", 2);
        OrderDTO orderDTO = OrderDTO.builder()
                .userId(userId)
                .items(List.of(itemDTO))
                .build();

        when(validationExecutor.validateAndThrow(orderDTO)).thenReturn(Set.of());
        when(userRepository.findById(userId)).thenReturn(user);
        when(productRepository.findById("1")).thenReturn(product);
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        Order result = createOrderUsecase.execute(orderDTO);

        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals(1, result.getItems().size());
        assertEquals(2000.0, result.getTotalPrice());
        assertEquals(OrderStatus.PENDING, result.getStatus());

        verify(validationExecutor, times(1)).validateAndThrow(orderDTO);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void givenMultipleItems_whenExecute_thenCalculatesTotalCorrectly() {
        String userId = UUID.randomUUID().toString();
        User user = new User(userId, "Raslan", "raslan@test.com", "+962791234567");

        Product product1 = Product.builder().id("1").name("Laptop").price(1000.0).build();
        Product product2 = Product.builder().id("2").name("Mouse").price(50.0).build();

        OrderItemDTO item1 = new OrderItemDTO("1", 2);
        OrderItemDTO item2 = new OrderItemDTO("2", 3);
        OrderDTO orderDTO = OrderDTO.builder()
                .userId(userId)
                .items(List.of(item1, item2))
                .build();

        when(validationExecutor.validateAndThrow(orderDTO)).thenReturn(Set.of());
        when(userRepository.findById(userId)).thenReturn(user);
        when(productRepository.findById("1")).thenReturn(product1);
        when(productRepository.findById("2")).thenReturn(product2);
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        Order result = createOrderUsecase.execute(orderDTO);

        assertEquals(2150.0, result.getTotalPrice());
        assertEquals(2, result.getItems().size());
    }

    @Test
    void givenInvalidOrderDTO_whenExecute_thenThrowsValidationException() {
        OrderDTO orderDTO = OrderDTO.builder()
                .userId(null)
                .items(List.of())
                .build();

        when(validationExecutor.validateAndThrow(orderDTO))
                .thenThrow(new ValidationException(Set.of()));

        assertThrows(ValidationException.class, () -> createOrderUsecase.execute(orderDTO));

        verify(orderRepository, never()).save(any(Order.class));
    }
}