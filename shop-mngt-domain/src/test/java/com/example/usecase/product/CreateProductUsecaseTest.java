package com.example.usecase.product;

import com.example.shop.domain.dto.ProductDTO;
import com.example.shop.domain.entity.Product;
import com.example.shop.domain.repository.ProductRepository;
import com.example.shop.domain.usecase.ValidationExecutor;
import com.example.shop.domain.usecase.ValidationException;
import com.example.shop.domain.usecase.product.CreateProductUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CreateProductUsecaseTest {

    private ProductRepository productRepository;
    private ValidationExecutor<ProductDTO> validationExecutor;
    private CreateProductUsecase createProductUsecase;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        validationExecutor = mock(ValidationExecutor.class);
        createProductUsecase = new CreateProductUsecase(productRepository, validationExecutor);
    }

    @Test
    void givenValidDTO_whenExecute_thenProductIsCreatedAndSaved() {
        ProductDTO dto = ProductDTO.builder()
                .name("Laptop")
                .price(1500.0)
                .description("Gaming laptop")
                .build();

        when(validationExecutor.validateAndThrow(dto)).thenReturn(Set.of());
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        Product result = createProductUsecase.execute(dto);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());
        assertEquals(1500.0, result.getPrice());
        assertEquals("Gaming laptop", result.getDescription());

        verify(validationExecutor, times(1)).validateAndThrow(dto);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void givenInvalidDTO_whenExecute_thenThrowsValidationException() {
        ProductDTO dto = ProductDTO.builder()
                .name("")
                .price(-100.0)
                .description("desc")
                .build();

        when(validationExecutor.validateAndThrow(dto))
                .thenThrow(new ValidationException(Set.of()));

        assertThrows(ValidationException.class, () -> createProductUsecase.execute(dto));

        verify(productRepository, never()).save(any(Product.class));
    }
}
