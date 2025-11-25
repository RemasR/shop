package com.example.shop.controller;

import com.example.shop.domain.dto.OrderDTO;
import com.example.shop.domain.dto.OrderItemDTO;
import com.example.shop.domain.entity.Order;
import com.example.shop.domain.entity.OrderItem;
import com.example.shop.domain.entity.OrderStatus;
import com.example.shop.domain.entity.Product;
import com.example.shop.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @Test
    void givenValidOrderDTO_whenCreateOrder_thenReturns200AndOrder() throws Exception {
        String userId = UUID.randomUUID().toString();
        String orderId = UUID.randomUUID().toString();

        OrderItemDTO itemDTO = new OrderItemDTO("1", 2);
        OrderDTO orderDTO = OrderDTO.builder()
                .userId(userId)
                .items(List.of(itemDTO))
                .build();

        Product product = Product.builder()
                .id("1")
                .name("Laptop")
                .price(1000.0)
                .build();

        OrderItem orderItem = new OrderItem(product, 2);

        Order createdOrder = Order.builder()
                .id(orderId)
                .userId(userId)
                .items(List.of(orderItem))
                .totalPrice(2000.0)
                .status(OrderStatus.PENDING)
                .build();

        when(orderService.createOrder(any(OrderDTO.class))).thenReturn(createdOrder);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(userId)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.totalPrice", is(2000.0)))
                .andExpect(jsonPath("$.status", is("PENDING")))
                .andExpect(jsonPath("$.id", notNullValue()));

        verify(orderService, times(1)).createOrder(any(OrderDTO.class));
    }

    @Test
    void givenValidOrderId_whenGetOrderById_thenReturns200AndOrder() throws Exception {
        String orderId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();

        Product product = Product.builder()
                .id("1")
                .name("Mouse")
                .price(50.0)
                .build();

        OrderItem orderItem = new OrderItem(product, 1);

        Order order = Order.builder()
                .id(orderId)
                .userId(userId)
                .items(List.of(orderItem))
                .totalPrice(50.0)
                .status(OrderStatus.PENDING)
                .build();

        when(orderService.getOrderById(orderId)).thenReturn(order);

        mockMvc.perform(get("/api/orders/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(orderId)))
                .andExpect(jsonPath("$.userId", is(userId)))
                .andExpect(jsonPath("$.totalPrice", is(50.0)))
                .andExpect(jsonPath("$.status", is("PENDING")));

        verify(orderService, times(1)).getOrderById(orderId);
    }

    @Test
    void givenValidUserId_whenGetUserOrders_thenReturns200AndOrders() throws Exception {
        String userId = UUID.randomUUID().toString();
        String orderId1 = UUID.randomUUID().toString();
        String orderId2 = UUID.randomUUID().toString();

        Product product = Product.builder()
                .id("1")
                .name("Keyboard")
                .price(75.0)
                .build();

        OrderItem orderItem = new OrderItem(product, 1);

        Order order1 = Order.builder()
                .id(orderId1)
                .userId(userId)
                .items(List.of(orderItem))
                .totalPrice(75.0)
                .status(OrderStatus.PENDING)
                .build();

        Order order2 = Order.builder()
                .id(orderId2)
                .userId(userId)
                .items(List.of(orderItem))
                .totalPrice(75.0)
                .status(OrderStatus.CONFIRMED)
                .build();

        List<Order> orders = Arrays.asList(order1, order2);

        when(orderService.getUserOrders(userId)).thenReturn(orders);

        mockMvc.perform(get("/api/orders/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userId", is(userId)))
                .andExpect(jsonPath("$[1].userId", is(userId)));

        verify(orderService, times(1)).getUserOrders(userId);
    }

    @Test
    void givenValidStatus_whenGetOrdersByStatus_thenReturns200AndOrders() throws Exception {
        String orderId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();

        Product product = Product.builder()
                .id("1")
                .name("Product")
                .price(100.0)
                .build();

        OrderItem orderItem = new OrderItem(product, 1);

        Order order = Order.builder()
                .id(orderId)
                .userId(userId)
                .items(List.of(orderItem))
                .totalPrice(100.0)
                .status(OrderStatus.PENDING)
                .build();

        when(orderService.getOrderByStatus(OrderStatus.PENDING)).thenReturn(List.of(order));

        mockMvc.perform(get("/api/orders/status/{status}", "PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].status", is("PENDING")));

        verify(orderService, times(1)).getOrderByStatus(OrderStatus.PENDING);
    }

    @Test
    void givenExistingOrder_whenUpdateOrder_thenReturns200AndUpdatedOrder() throws Exception {
        String orderId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();

        OrderItemDTO itemDTO = new OrderItemDTO("2", 2);
        OrderDTO updateDTO = OrderDTO.builder()
                .userId(userId)
                .items(List.of(itemDTO))
                .build();

        Product product = Product.builder()
                .id("2")
                .name("Updated Product")
                .price(200.0)
                .build();

        OrderItem orderItem = new OrderItem(product, 2);

        Order updatedOrder = Order.builder()
                .id(orderId)
                .userId(userId)
                .items(List.of(orderItem))
                .totalPrice(400.0)
                .status(OrderStatus.PENDING)
                .build();

        when(orderService.updateOrder(eq(orderId), any(OrderDTO.class))).thenReturn(updatedOrder);

        mockMvc.perform(put("/api/orders/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(orderId)))
                .andExpect(jsonPath("$.totalPrice", is(400.0)));

        verify(orderService, times(1)).updateOrder(eq(orderId), any(OrderDTO.class));
    }

    @Test
    void givenExistingOrder_whenDeleteOrder_thenReturns200() throws Exception {
        String orderId = UUID.randomUUID().toString();

        doNothing().when(orderService).cancelOrder(orderId);

        mockMvc.perform(delete("/api/orders/{id}", orderId))
                .andExpect(status().isOk());

        verify(orderService, times(1)).cancelOrder(orderId);
    }

    @Test
    void givenOrders_whenGetAllOrders_thenReturns200AndOrderList() throws Exception {
        String orderId1 = UUID.randomUUID().toString();
        String orderId2 = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();

        Product product = Product.builder()
                .id("1")
                .name("Product")
                .price(100.0)
                .build();

        OrderItem orderItem = new OrderItem(product, 1);

        Order order1 = Order.builder()
                .id(orderId1)
                .userId(userId)
                .items(List.of(orderItem))
                .totalPrice(100.0)
                .status(OrderStatus.PENDING)
                .build();

        Order order2 = Order.builder()
                .id(orderId2)
                .userId(userId)
                .items(List.of(orderItem))
                .totalPrice(100.0)
                .status(OrderStatus.CONFIRMED)
                .build();

        List<Order> orders = Arrays.asList(order1, order2);

        when(orderService.getAllOrders()).thenReturn(orders);

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    void givenNoOrders_whenGetAllOrders_thenReturns200AndEmptyList() throws Exception {
        when(orderService.getAllOrders()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(orderService, times(1)).getAllOrders();
    }
}