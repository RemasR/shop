package com.example.product;

import com.example.shop.domain.dto.ProductDTO;
import com.example.shop.domain.entity.Product;
import com.example.shop.domain.repository.ProductRepository;
import com.example.shop.domain.usecase.product.UpdateProductUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UpdateProductUsecaseTest {

    private ProductRepository productRepository;
    private UpdateProductUsecase updateProductUsecase;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        updateProductUsecase = new UpdateProductUsecase(productRepository);
    }

    @Test
    void givenValidDTO_whenUpdateProduct_shouldUpdateAndReturnSavedProduct() {
        int productId = 1;

        Product existingProduct = Product.builder()
                .id(productId)
                .name("Old Name")
                .price(100.0)
                .description("Old Description")
                .build();

        ProductDTO dto = ProductDTO.builder()
                .name("New Name")
                .price(150.0)
                .description("New Description")
                .build();

        Product savedProduct = Product.builder()
                .id(productId)
                .name("New Name")
                .price(150.0)
                .description("New Description")
                .build();

        when(productRepository.findById(productId)).thenReturn(existingProduct);
        when(productRepository.existsById(productId)).thenReturn(true);
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        Product result = updateProductUsecase.execute(productId, dto);

        assertNotNull(result);
        assertEquals("New Name", result.getName());
        assertEquals(150.0, result.getPrice());
        assertEquals("New Description", result.getDescription());
        verify(productRepository).findById(productId);
        verify(productRepository).save(existingProduct);
    }

    @Test
    void givenPartialDTO_whenUpdateProduct_shouldOnlyUpdateNonNullFields() {
        int productId = 2;

        Product existingProduct = Product.builder()
                .id(productId)
                .name("Old Name")
                .price(100.0)
                .description("Old Description")
                .build();

        ProductDTO dto = ProductDTO.builder()
                .price(200.0)
                .build();

        when(productRepository.findById(productId)).thenReturn(existingProduct);
        when(productRepository.existsById(productId)).thenReturn(true);
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        Product result = updateProductUsecase.execute(productId, dto);

        assertEquals("Old Name", result.getName());
        assertEquals(200.0, result.getPrice());
        assertEquals("Old Description", result.getDescription());
        verify(productRepository).save(existingProduct);
    }

    @Test
    void givenNullDTO_whenUpdateProduct_shouldThrowException() {
        int productId = 3;
        assertThrows(IllegalArgumentException.class, () -> updateProductUsecase.execute(productId, null));
        verify(productRepository, never()).findById(anyInt());
        verify(productRepository, never()).save(any());
    }

    @Test
    void givenInvalidProductName_whenUpdateProduct_shouldThrowValidationException() {
        int productId = 4;

        Product existingProduct = Product.builder()
                .id(productId)
                .name("Valid Name")
                .price(100.0)
                .description("desc")
                .build();

        ProductDTO dto = ProductDTO.builder()
                .name("") // invalid
                .build();

        when(productRepository.findById(productId)).thenReturn(existingProduct);
        when(productRepository.existsById(productId)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> updateProductUsecase.execute(productId, dto));

        verify(productRepository, never()).save(any());
    }

    @Test
    void givenInvalidProductPrice_whenUpdateProduct_shouldThrowValidationException() {
        int productId = 5;

        Product existingProduct = Product.builder()
                .id(productId)
                .name("Product")
                .price(100.0)
                .description("desc")
                .build();

        ProductDTO dto = ProductDTO.builder()
                .price(-50.0)
                .build();

        when(productRepository.findById(productId)).thenReturn(existingProduct);
        when(productRepository.existsById(productId)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> updateProductUsecase.execute(productId, dto));

        verify(productRepository, never()).save(any());
    }

    @Test
    void givenRepositoryThrowsException_whenUpdateProduct_shouldPropagateException() {
        int productId = 6;

        Product existingProduct = Product.builder()
                .id(productId)
                .name("Product")
                .price(100.0)
                .description("desc")
                .build();

        ProductDTO dto = ProductDTO.builder()
                .name("Updated")
                .price(200.0)
                .build();

        when(productRepository.findById(productId)).thenReturn(existingProduct);
        when(productRepository.existsById(productId)).thenReturn(true);
        when(productRepository.save(any(Product.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> updateProductUsecase.execute(productId, dto));

        verify(productRepository).findById(productId);
        verify(productRepository).save(any(Product.class));
    }
}
