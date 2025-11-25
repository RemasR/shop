package com.example.shop.controller;

import com.example.shop.domain.dto.ProductDTO;
import com.example.shop.domain.entity.Product;
import com.example.shop.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    void givenValidProductDTO_whenCreateProduct_thenReturns200AndProduct() throws Exception {
        ProductDTO dto = ProductDTO.builder()
                .name("Laptop")
                .price(1500.0)
                .description("Gaming laptop")
                .build();

        Product createdProduct = Product.builder()
                .id("1")
                .name("Laptop")
                .price(1500.0)
                .description("Gaming laptop")
                .build();

        when(productService.createProduct(any(ProductDTO.class))).thenReturn(createdProduct);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Laptop")))
                .andExpect(jsonPath("$.price", is(1500.0)))
                .andExpect(jsonPath("$.description", is("Gaming laptop")))
                .andExpect(jsonPath("$.id", notNullValue()));

        verify(productService, times(1)).createProduct(any(ProductDTO.class));
    }

    @Test
    void givenValidProductId_whenGetProductById_thenReturns200AndProduct() throws Exception {
        String productId = "1";
        Product product = Product.builder()
                .id(productId)
                .name("Mouse")
                .price(50.0)
                .description("Wireless mouse")
                .build();

        when(productService.getProductById(productId)).thenReturn(product);

        mockMvc.perform(get("/api/products/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(productId)))
                .andExpect(jsonPath("$.name", is("Mouse")))
                .andExpect(jsonPath("$.price", is(50.0)))
                .andExpect(jsonPath("$.description", is("Wireless mouse")));

        verify(productService, times(1)).getProductById(productId);
    }

    @Test
    void givenExistingProduct_whenUpdateProduct_thenReturns200AndUpdatedProduct() throws Exception {
        String productId = "1";
        ProductDTO updateDTO = ProductDTO.builder()
                .name("New Name")
                .price(150.0)
                .description("New description")
                .build();

        Product updatedProduct = Product.builder()
                .id(productId)
                .name("New Name")
                .price(150.0)
                .description("New description")
                .build();

        when(productService.updateProduct(eq(productId), any(ProductDTO.class))).thenReturn(updatedProduct);

        mockMvc.perform(put("/api/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(productId)))
                .andExpect(jsonPath("$.name", is("New Name")))
                .andExpect(jsonPath("$.price", is(150.0)))
                .andExpect(jsonPath("$.description", is("New description")));

        verify(productService, times(1)).updateProduct(eq(productId), any(ProductDTO.class));
    }

    @Test
    void givenExistingProduct_whenUpdateOnlyName_thenReturns200AndUpdatedProduct() throws Exception {
        String productId = "1";
        ProductDTO updateDTO = ProductDTO.builder()
                .name("New Name Only")
                .build();

        Product updatedProduct = Product.builder()
                .id(productId)
                .name("New Name Only")
                .price(100.0)
                .description("Old description")
                .build();

        when(productService.updateProduct(eq(productId), any(ProductDTO.class))).thenReturn(updatedProduct);

        mockMvc.perform(put("/api/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(productId)))
                .andExpect(jsonPath("$.name", is("New Name Only")))
                .andExpect(jsonPath("$.price", is(100.0)))
                .andExpect(jsonPath("$.description", is("Old description")));

        verify(productService, times(1)).updateProduct(eq(productId), any(ProductDTO.class));
    }

    @Test
    void givenExistingProduct_whenDeleteProduct_thenReturns200() throws Exception {
        String productId = "1";

        doNothing().when(productService).deleteProduct(productId);

        mockMvc.perform(delete("/api/products/{id}", productId))
                .andExpect(status().isOk());

        verify(productService, times(1)).deleteProduct(productId);
    }

    @Test
    void givenProducts_whenGetAllProducts_thenReturns200AndProductList() throws Exception {
        Product product1 = Product.builder()
                .id("1")
                .name("Product 1")
                .price(100.0)
                .description("First product")
                .build();

        Product product2 = Product.builder()
                .id("2")
                .name("Product 2")
                .price(200.0)
                .description("Second product")
                .build();

        List<Product> products = Arrays.asList(product1, product2);

        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Product 1")))
                .andExpect(jsonPath("$[1].name", is("Product 2")));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void givenNoProducts_whenGetAllProducts_thenReturns200AndEmptyList() throws Exception {
        when(productService.getAllProducts()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(productService, times(1)).getAllProducts();
    }
}