package com.example.usecase.product;

import com.example.shop.domain.entity.Product;
import com.example.shop.domain.repository.ProductRepository;
import com.example.shop.domain.usecase.product.ListAllProductUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ListAllProductUsecaseTest {

    private ProductRepository productRepository;
    private ListAllProductUsecase listAllProductUsecase;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        listAllProductUsecase = new ListAllProductUsecase(productRepository);
    }

    @Test
    void givenProductsExist_whenExecute_shouldReturnAllProducts() {
        Product p1 = Product.builder()
                .id(1)
                .name("Laptop")
                .price(1500.0)
                .description("Gaming laptop")
                .build();

        Product p2 = Product.builder()
                .id(2)
                .name("Phone")
                .price(700.0)
                .description("Smartphone")
                .build();

        List<Product> products = List.of(p1, p2);

        when(productRepository.findAllProducts()).thenReturn(products);

        List<Product> result = listAllProductUsecase.execute();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Laptop", result.get(0).getName());
        assertEquals("Phone", result.get(1).getName());

        verify(productRepository, times(1)).findAllProducts();
    }

    @Test
    void givenNoProductsExist_whenExecute_shouldReturnEmptyList() {
        when(productRepository.findAllProducts()).thenReturn(List.of());

        List<Product> result = listAllProductUsecase.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(productRepository, times(1)).findAllProducts();
    }

}
