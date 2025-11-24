package com.example.usecase.product;

import com.example.shop.domain.dto.ProductDTO;
import com.example.shop.domain.entity.Product;
import com.example.shop.domain.repository.ProductRepository;
import com.example.shop.domain.usecase.ValidationExecutor;
import com.example.shop.domain.usecase.ValidationException;
import com.example.shop.domain.usecase.product.CreateProductUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CreateProductUsecaseTest {

    private ProductRepository productRepository;
    private ValidationExecutor<Product> validationExecutor;
    private CreateProductUsecase createProductUsecase;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        validationExecutor = mock(ValidationExecutor.class);
        createProductUsecase = new CreateProductUsecase(productRepository, validationExecutor);
    }

    @Test
    void givenValidProduct_whenExecute_thenProductIsCreatedAndSaved() {
        ProductDTO dto = ProductDTO.builder()
                .name("Laptop")
                .price(1500.0)
                .description("Gaming laptop")
                .build();

        when(productRepository.save(any(Product.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Product result = createProductUsecase.execute(dto);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(validationExecutor, times(1)).validateAndThrow(productCaptor.capture());
        verify(productRepository, times(1)).save(any(Product.class));

        Product validatedProduct = productCaptor.getValue();
        assertEquals("Laptop", validatedProduct.getName());
        assertEquals(1500.0, validatedProduct.getPrice());
        assertEquals("Gaming laptop", validatedProduct.getDescription());
    }

    @Test
    void givenInvalidProduct_whenExecute_thenThrowsValidationException() {
        ProductDTO dto = ProductDTO.builder()
                .name("")
                .price(-100.0)
                .description("desc")
                .build();

        Set violations = Set.of();

        doThrow(new ValidationException(violations))
                .when(validationExecutor).validateAndThrow(any(Product.class));

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> createProductUsecase.execute(dto)
        );

        verify(productRepository, never()).save(any(Product.class));
        verify(validationExecutor, times(1)).validateAndThrow(any(Product.class));
    }
}
