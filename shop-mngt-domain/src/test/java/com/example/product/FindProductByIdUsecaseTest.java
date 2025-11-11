package com.example.product;

import com.example.shop.domain.entity.Product;
import com.example.shop.domain.repository.ProductRepository;
import com.example.shop.domain.usecase.product.FindProductByIdUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FindProductByIdUsecaseTest {

    private ProductRepository productRepository;
    private FindProductByIdUsecase findProductByIdUsecase;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        findProductByIdUsecase = new FindProductByIdUsecase(productRepository);
    }

    @Test
    void givenExistingProduct_whenExecute_shouldReturnProduct() {
        Product product = Product.builder()
                .id(1)
                .name("Laptop")
                .price(1500.0)
                .description("Gaming laptop")
                .build();

        when(productRepository.existsById(1)).thenReturn(true);
        when(productRepository.findById(1)).thenReturn(product);

        Product result = findProductByIdUsecase.execute(product.getId());

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Laptop", result.getName());
        assertEquals(1500.0, result.getPrice());

        verify(productRepository, times(1)).existsById(1);
        verify(productRepository, times(1)).findById(1);
        verify(productRepository, never()).save(any());
    }

    @Test
    void givenNonExistingProductId_whenFindById_thenThrowsException() {

        Integer nonExistingProductId = -1;

        when(productRepository.existsById(nonExistingProductId)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> findProductByIdUsecase.execute(nonExistingProductId)
        );

        assertTrue(exception.getMessage().contains("does not exist"));

        verify(productRepository, times(1)).existsById(nonExistingProductId);
        verify(productRepository, never()).findById(nonExistingProductId);
    }
}
