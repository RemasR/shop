package com.example.shop.controller;

import com.example.shop.ShopApplication;
import com.example.shop.config.UsecaseConfig;
import com.example.shop.domain.dto.ProductDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {ShopApplication.class, UsecaseConfig.class, GlobalExceptionHandler.class}
)
public class ProductControllerTest {
    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    void givenValidProductDTO_whenCreateProduct_thenReturns200AndProduct() {
        ProductDTO dto = ProductDTO.builder()
                .name("Laptop")
                .price(1500.0)
                .description("Gaming laptop")
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/api/products")
                .then()
                .statusCode(200)
                .body("name", equalTo("Laptop"))
                .body("price", equalTo(1500.0f))
                .body("description", equalTo("Gaming laptop"))
                .body("id", notNullValue());
    }

    @Test
    void givenShortName_whenCreateProduct_thenReturns400() {
        ProductDTO dto = ProductDTO.builder()
                .name("AB")
                .price(100.0)
                .description("Too short name")
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/api/products")
                .then()
                .statusCode(400);
    }

    @Test
    void givenNullName_whenCreateProduct_thenReturns400() {
        ProductDTO dto = ProductDTO.builder()
                .name(null)
                .price(100.0)
                .description("No name")
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/api/products")
                .then()
                .statusCode(400);
    }

    @Test
    void givenEmptyName_whenCreateProduct_thenReturns400() {
        ProductDTO dto = ProductDTO.builder()
                .name("")
                .price(100.0)
                .description("Empty name")
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/api/products")
                .then()
                .statusCode(400);
    }

    @Test
    void givenNegativePrice_whenCreateProduct_thenReturns400() {
        ProductDTO dto = ProductDTO.builder()
                .name("Product")
                .price(-10.0)
                .description("Negative price")
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/api/products")
                .then()
                .statusCode(400);
    }

    @Test
    void givenZeroPrice_whenCreateProduct_thenReturns400() {
        ProductDTO dto = ProductDTO.builder()
                .name("Product")
                .price(0.0)
                .description("Zero price")
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/api/products")
                .then()
                .statusCode(400);
    }

    @Test
    void givenPriceExceedingMaximum_whenCreateProduct_thenReturns400() {
        ProductDTO dto = ProductDTO.builder()
                .name("Product")
                .price(1_500_000.0)
                .description("Price too high")
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/api/products")
                .then()
                .statusCode(400);
    }

    @Test
    void givenValidProductId_whenGetProductById_thenReturns200AndProduct() {
        ProductDTO createDTO = ProductDTO.builder()
                .name("Mouse")
                .price(50.0)
                .description("Wireless mouse")
                .build();

        String productId = given()
                .contentType(ContentType.JSON)
                .body(createDTO)
                .when()
                .post("/api/products")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        given()
                .when()
                .get("/api/products/" + productId)
                .then()
                .statusCode(200)
                .body("id", equalTo(productId))
                .body("name", equalTo("Mouse"))
                .body("price", equalTo(50.0f))
                .body("description", equalTo("Wireless mouse"));
    }

    @Test
    void givenNonExistingProductId_whenGetProductById_thenReturns400() {
        given()
                .when()
                .get("/api/products/non-existing-product-id")
                .then()
                .statusCode(400);
    }

    @Test
    void givenExistingProduct_whenUpdateProduct_thenReturns200AndUpdatedProduct() {
        ProductDTO createDTO = ProductDTO.builder()
                .name("Old Name")
                .price(100.0)
                .description("Old description")
                .build();

        String productId = given()
                .contentType(ContentType.JSON)
                .body(createDTO)
                .when()
                .post("/api/products")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        ProductDTO updateDTO = ProductDTO.builder()
                .name("New Name")
                .price(150.0)
                .description("New description")
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(updateDTO)
                .when()
                .put("/api/products/" + productId)
                .then()
                .statusCode(200)
                .body("id", equalTo(productId))
                .body("name", equalTo("New Name"))
                .body("price", equalTo(150.0f))
                .body("description", equalTo("New description"));
    }

    @Test
    void givenExistingProduct_whenUpdateOnlyName_thenReturns200AndUpdatedProduct() {
        ProductDTO createDTO = ProductDTO.builder()
                .name("Old Name")
                .price(100.0)
                .description("Old description")
                .build();

        String productId = given()
                .contentType(ContentType.JSON)
                .body(createDTO)
                .when()
                .post("/api/products")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        ProductDTO updateDTO = ProductDTO.builder()
                .name("New Name Only")
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(updateDTO)
                .when()
                .put("/api/products/" + productId)
                .then()
                .statusCode(200)
                .body("id", equalTo(productId))
                .body("name", equalTo("New Name Only"))
                .body("price", equalTo(100.0f))
                .body("description", equalTo("Old description"));
    }

    @Test
    void givenExistingProduct_whenUpdateOnlyPrice_thenReturns200AndUpdatedProduct() {
        ProductDTO createDTO = ProductDTO.builder()
                .name("Product Name")
                .price(100.0)
                .description("Product description")
                .build();

        String productId = given()
                .contentType(ContentType.JSON)
                .body(createDTO)
                .when()
                .post("/api/products")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        ProductDTO updateDTO = ProductDTO.builder()
                .price(200.0)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(updateDTO)
                .when()
                .put("/api/products/" + productId)
                .then()
                .statusCode(200)
                .body("id", equalTo(productId))
                .body("name", equalTo("Product Name"))
                .body("price", equalTo(200.0f))
                .body("description", equalTo("Product description"));
    }

    @Test
    void givenExistingProduct_whenUpdateOnlyDescription_thenReturns200AndUpdatedProduct() {
        ProductDTO createDTO = ProductDTO.builder()
                .name("Product Name")
                .price(100.0)
                .description("Old description")
                .build();

        String productId = given()
                .contentType(ContentType.JSON)
                .body(createDTO)
                .when()
                .post("/api/products")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        ProductDTO updateDTO = ProductDTO.builder()
                .description("New description")
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(updateDTO)
                .when()
                .put("/api/products/" + productId)
                .then()
                .statusCode(200)
                .body("id", equalTo(productId))
                .body("name", equalTo("Product Name"))
                .body("price", equalTo(100.0f))
                .body("description", equalTo("New description"));
    }

    @Test
    void givenNonExistingProductId_whenUpdateProduct_thenReturns400() {
        ProductDTO updateDTO = ProductDTO.builder()
                .name("New Name")
                .price(150.0)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(updateDTO)
                .when()
                .put("/api/products/non-existing-id")
                .then()
                .statusCode(400);
    }

    @Test
    void givenInvalidNameOnUpdate_whenUpdateProduct_thenReturns400() {
        ProductDTO createDTO = ProductDTO.builder()
                .name("Valid Name")
                .price(100.0)
                .description("Description")
                .build();

        String productId = given()
                .contentType(ContentType.JSON)
                .body(createDTO)
                .when()
                .post("/api/products")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        ProductDTO updateDTO = ProductDTO.builder()
                .name("AB")
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(updateDTO)
                .when()
                .put("/api/products/" + productId)
                .then()
                .statusCode(400);
    }

    @Test
    void givenInvalidPriceOnUpdate_whenUpdateProduct_thenReturns400() {
        ProductDTO createDTO = ProductDTO.builder()
                .name("Valid Name")
                .price(100.0)
                .description("Description")
                .build();

        String productId = given()
                .contentType(ContentType.JSON)
                .body(createDTO)
                .when()
                .post("/api/products")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        ProductDTO updateDTO = ProductDTO.builder()
                .price(-50.0)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(updateDTO)
                .when()
                .put("/api/products/" + productId)
                .then()
                .statusCode(400);
    }

    @Test
    void givenExistingProduct_whenDeleteProduct_thenReturns200AndProductIsDeleted() {
        ProductDTO createDTO = ProductDTO.builder()
                .name("To Delete")
                .price(50.0)
                .description("Will be deleted")
                .build();

        String productId = given()
                .contentType(ContentType.JSON)
                .body(createDTO)
                .when()
                .post("/api/products")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        given()
                .when()
                .delete("/api/products/" + productId)
                .then()
                .statusCode(200);

        given()
                .when()
                .get("/api/products/" + productId)
                .then()
                .statusCode(400);
    }

    @Test
    void givenNonExistingProductId_whenDeleteProduct_thenReturns400() {
        given()
                .when()
                .delete("/api/products/non-existing-id")
                .then()
                .statusCode(400);
    }

    @Test
    void givenProducts_whenGetAllProducts_thenReturns200AndProductList() {
        ProductDTO product1 = ProductDTO.builder()
                .name("Product 1")
                .price(100.0)
                .description("First product")
                .build();

        ProductDTO product2 = ProductDTO.builder()
                .name("Product 2")
                .price(200.0)
                .description("Second product")
                .build();

        given().
                contentType(ContentType.JSON)
                .body(product1)
                .when()
                .post("/api/products");
        given()
                .contentType(ContentType.JSON)
                .body(product2)
                .when()
                .post("/api/products");

        given()
                .when()
                .get("/api/products")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(2));
    }

    @Test
    void givenNoProducts_whenGetAllProducts_thenReturns200AndEmptyList() {
        given()
                .when()
                .get("/api/products")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test
    void givenValidProductWithMinimumPrice_whenCreateProduct_thenReturns200() {
        ProductDTO dto = ProductDTO.builder()
                .name("Cheap Item")
                .price(0.01)
                .description("Minimum price")
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/api/products")
                .then()
                .statusCode(200)
                .body("price", equalTo(0.01f));
    }

    @Test
    void givenValidProductWithMaximumPrice_whenCreateProduct_thenReturns200() {
        ProductDTO dto = ProductDTO.builder()
                .name("Expensive Item")
                .price(1_000_000.0)
                .description("Maximum price")
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/api/products")
                .then()
                .statusCode(200)
                .body("price", equalTo(1_000_000.0f));
    }

    @Test
    void givenValidProductWithMinimumNameLength_whenCreateProduct_thenReturns200() {
        ProductDTO dto = ProductDTO.builder()
                .name("ABC")
                .price(50.0)
                .description("Minimum name length")
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/api/products")
                .then()
                .statusCode(200)
                .body("name", equalTo("ABC"));
    }
}