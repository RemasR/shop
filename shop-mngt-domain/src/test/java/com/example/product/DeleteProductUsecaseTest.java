package com.example.product;

import com.example.shop.domain.repository.ProductRepository;
import com.example.shop.domain.usecase.product.DeleteProductUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class DeleteProductUsecaseTest {

    private ProductRepository productRepository;
    private DeleteProductUsecase deleteProductUsecase;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        deleteProductUsecase = new DeleteProductUsecase(productRepository);
    }

    @Test
    void givenValidId_whenDeleteProduct_shouldCallRepositoryDeleteByIdOnce() {
        int productId = 1;

        when(productRepository.existsById(productId)).thenReturn(true);

        deleteProductUsecase.execute(productId);

        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    void givenNonExistingId_whenDeleteProduct_shouldThrowValidationException() {
        int productId = 99;

        when(productRepository.existsById(productId)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> deleteProductUsecase.execute(productId)
        );

        assertTrue(exception.getMessage().contains("Validation failed"));

        verify(productRepository, never()).deleteById(anyInt());
    }

    @Test
    void givenNegativeId_whenDeleteProduct_shouldThrowValidationException() {
        int productId = -5;

        when(productRepository.existsById(productId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> deleteProductUsecase.execute(productId));

        verify(productRepository, never()).deleteById(anyInt());
    }

    @Test
    void givenZeroId_whenDeleteProduct_shouldThrowValidationException() {
        int productId = 0;

        when(productRepository.existsById(productId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> deleteProductUsecase.execute(productId));

        verify(productRepository, never()).deleteById(anyInt());
    }
}
