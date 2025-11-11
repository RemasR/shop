package com.example.product;

import com.example.shop.domain.entity.Product;
import com.example.shop.domain.repository.ProductRepository;
import com.example.shop.domain.usecase.product.CreateProductUsecase;
import com.example.shop.domain.dto.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CreateProductUsecaseTest {

    private ProductRepository productRepository;
    private CreateProductUsecase createProductUsecase;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        createProductUsecase = new CreateProductUsecase(productRepository);
    }

    @Test
    void givenValidDTO_whenCreateProduct_shouldCreateProductSuccessfully() {
        ProductDTO dto = ProductDTO.builder()
                .name("Laptop")
                .price(1500.0)
                .description("Gaming laptop")
                .build();

        Product saved = Product.builder()
                .id(1)
                .name("Laptop")
                .price(1500.0)
                .description("Gaming laptop")
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(saved);

        Product result = createProductUsecase.execute(dto);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());
        assertEquals(1500.0, result.getPrice());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void givenInvalidDTO_whenCreateProduct_shouldThrowExceptionWhenDTOIsNull() {
        assertThrows(IllegalArgumentException.class, () -> createProductUsecase.execute(null));
        verify(productRepository, never()).save(any());
    }

    @Test
    void givenEmptyName_whenCreateProduct_shouldThrowExceptionWhenNameIsBlank() {
        ProductDTO dto = ProductDTO.builder()
                .name(" ")
                .price(100.0)
                .description("desc")
                .build();

        assertThrows(IllegalArgumentException.class, () -> createProductUsecase.execute(dto));
        verify(productRepository, never()).save(any());
    }

    @Test
    void givenNegativePrice_whenCreateProduct_shouldThrowExceptionWhenPriceIsNegative() {
        ProductDTO dto = ProductDTO.builder()
                .name("Phone")
                .price(-10.0)
                .description("desc")
                .build();

        assertThrows(IllegalArgumentException.class, () -> createProductUsecase.execute(dto));
        verify(productRepository, never()).save(any());
    }
}
