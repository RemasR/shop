package com.example.shop.domain.model;

import java.util.List;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private String userId;
    private String id;
    private List<OrderItem> items;
    private double totalPrice;
    private OrderStatus status;
}