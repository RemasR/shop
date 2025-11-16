package com.example.usecase.product;

import com.example.shop.domain.dto.ProductDTO;
import com.example.shop.domain.entity.Product;
import com.example.shop.domain.repository.ProductRepository;
import com.example.shop.domain.usecase.ValidationExecutor;
import com.example.shop.domain.usecase.ValidationException;
import com.example.shop.domain.usecase.product.UpdateProductUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UpdateProductUsecaseTest {

    private ProductRepository productRepository;
    private ValidationExecutor<String> idValidator;
    private ValidationExecutor<Product> productValidator;
    private UpdateProductUsecase updateProductUsecase;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        idValidator = mock(ValidationExecutor.class);
        productValidator = mock(ValidationExecutor.class);
        updateProductUsecase = new UpdateProductUsecase(productRepository, idValidator, productValidator);
    }

    @Test
    void givenValidDTO_whenExecute_thenProductIsUpdatedAndSaved() {
        String productId = "1";

        Product existing = Product.builder()
                .id(productId)
                .name("Old Name")
                .price(100.0)
                .description("Old Desc")
                .build();

        ProductDTO dto = ProductDTO.builder()
                .name("New Name")
                .price(200.0)
                .description("New Desc")
                .build();

        when(idValidator.validateAndThrow(productId)).thenReturn(Set.of());
        when(productValidator.validateAndThrow(existing)).thenReturn(Set.of());
        when(productRepository.findById(productId)).thenReturn(existing);
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        Product result = updateProductUsecase.execute(productId, dto);

        assertEquals("New Name", result.getName());
        assertEquals(200.0, result.getPrice());
        assertEquals("New Desc", result.getDescription());

        verify(idValidator, times(1)).validateAndThrow(productId);
        verify(productValidator, times(1)).validateAndThrow(existing);
        verify(productRepository, times(1)).save(existing);
    }

    @Test
    void givenInvalidDTO_whenExecute_thenThrowsValidationException() {
        String productId = "2";

        Product existing = Product.builder()
                .id(productId)
                .name("Old Name")
                .price(100.0)
                .description("Old Desc")
                .build();

        ProductDTO dto = ProductDTO.builder()
                .name("")
                .build();

        when(idValidator.validateAndThrow(productId)).thenReturn(Set.of());
        when(productValidator.validateAndThrow(existing))
                .thenThrow(new ValidationException(Set.of()));
        when(productRepository.findById(productId)).thenReturn(existing);

        assertThrows(ValidationException.class, () -> updateProductUsecase.execute(productId, dto));
        verify(productRepository, never()).save(any());
    }
}