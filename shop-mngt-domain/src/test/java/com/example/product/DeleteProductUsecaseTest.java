package com.example.product;

import com.example.shop.domain.repository.ProductRepository;
import com.example.shop.domain.usecase.ValidationExecutor;
import com.example.shop.domain.usecase.ValidationException;
import com.example.shop.domain.usecase.product.DeleteProductUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class DeleteProductUsecaseTest {

    private ProductRepository productRepository;
    private ValidationExecutor<Integer> validationExecutor;
    private DeleteProductUsecase deleteProductUsecase;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        validationExecutor = mock(ValidationExecutor.class);
        deleteProductUsecase = new DeleteProductUsecase(productRepository, validationExecutor);
    }

    @Test
    void givenValidId_whenExecute_thenProductIsDeleted() {
        int productId = 1;

        when(validationExecutor.validateAndThrow(productId)).thenReturn(Set.of());

        deleteProductUsecase.execute(productId);

        verify(validationExecutor, times(1)).validateAndThrow(productId);
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    void givenInvalidId_whenExecute_thenThrowsValidationException() {
        int invalidId = -1;

        doThrow(new ValidationException(Set.of()))
                .when(validationExecutor).validateAndThrow(invalidId);

        assertThrows(ValidationException.class, () -> deleteProductUsecase.execute(invalidId));

        verify(productRepository, never()).deleteById(anyInt());
    }
}
