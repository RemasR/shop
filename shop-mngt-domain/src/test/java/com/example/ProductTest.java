package com.example;

import com.example.shop.domain.entity.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductTest {
    private Product product;
    @Test
    public void givenValidProductData_whenCreateProduct_thenProductIsCreated() {
        product = new Product(1, "T-Shirt",
                "black tshirt",5.0);

        assertEquals(1, product.getId());
    }

    @Test
    public void givenNullOrEmptyName_whenSetName_thenThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Product(1,"","Description", 5.5);
        });
    }

    @Test
    public void givenNameTooLong_whenSetName_thenThrowsException() {
        product = new Product(1, "T-Shirt", "black tshirt", 5.0);

        String longName = "a".repeat(31);

        assertThrows(IllegalArgumentException.class, () -> {
            product.setName(longName);
        });
    }

    @Test
    public void givenNegativePrice_whenSetPrice_thenThrowsException() {
        product = new Product(1, "T-Shirt", "black tshirt", 5.0);

        assertThrows(IllegalArgumentException.class, () -> {
            product.setPrice(-1.0);
        });
    }

    @Test
    public void givenValidName_whenSetName_thenNameIsUpdated() {
        product = new Product(1, "T-Shirt", "black tshirt", 5.0);

        product.setName("Jeans");

        assertEquals("Jeans", product.getName());
    }

    @Test
    public void givenValidPrice_whenSetPrice_thenPriceIsUpdated() {
        product = new Product(1, "T-Shirt", "black tshirt", 5.0);

        product.setPrice(10.5);

        assertEquals(10.5, product.getPrice());
    }
}