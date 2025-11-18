package com.example.usecase.product;

import com.example.shop.domain.entity.Product;
import com.example.shop.domain.repository.ProductRepository;
import com.example.shop.domain.usecase.ValidationException;
import com.example.shop.domain.usecase.ValidationExecutor;
import com.example.shop.domain.usecase.product.FindProductByIdUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class FindProductByIdUsecaseTest {

    private ProductRepository productRepository;
    private ValidationExecutor<String> validationExecutor;
    private FindProductByIdUsecase findProductByIdUsecase;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        validationExecutor = mock(ValidationExecutor.class);
        findProductByIdUsecase = new FindProductByIdUsecase(productRepository, validationExecutor);
    }

    @Test
    void givenValidId_whenExecute_thenReturnsProduct() {
        String productId = "1";

        Product product = Product.builder()
                .id(productId)
                .name("Laptop")
                .price(1500.0)
                .description("Gaming laptop")
                .build();

        when(validationExecutor.validateAndThrow(productId)).thenReturn(Set.of());
        when(productRepository.findById(productId)).thenReturn(product);

        Product result = findProductByIdUsecase.execute(productId);

        assertEquals(product, result);
        verify(validationExecutor, times(1)).validateAndThrow(productId);
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void givenInvalidId_whenExecute_thenThrowsValidationException() {
        String invalidId = "-1";

        when(validationExecutor.validateAndThrow(invalidId))
                .thenThrow(new ValidationException(Set.of()));

        assertThrows(ValidationException.class, () -> findProductByIdUsecase.execute(invalidId));

        verify(productRepository, never()).findById(anyString());
    }
}
