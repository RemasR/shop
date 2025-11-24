package com.example.shop.controller;

import com.example.shop.ShopApplication;
import com.example.shop.config.UsecaseConfig;
import com.example.shop.domain.dto.OrderDTO;
import com.example.shop.domain.dto.OrderItemDTO;
import com.example.shop.domain.dto.ProductDTO;
import com.example.shop.domain.dto.UserDTO;
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
public class OrderControllerTest {
    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    void givenValidOrderDTO_whenCreateOrder_thenReturns200AndOrder() {
        UserDTO userDTO = UserDTO.builder()
                .name("Ahmad")
                .email("ahmad@test.com")
                .phoneNumber("+962791234567")
                .build();

        String userId = given()
                .contentType(ContentType.JSON)
                .body(userDTO)
                .when()
                .post("/api/users")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        ProductDTO productDTO = ProductDTO.builder()
                .name("Laptop")
                .price(1000.0)
                .description("Gaming laptop")
                .build();

        String productId = given()
                .contentType(ContentType.JSON)
                .body(productDTO)
                .when()
                .post("/api/products")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        OrderItemDTO itemDTO = new OrderItemDTO(productId, 2);
        OrderDTO orderDTO = OrderDTO.builder()
                .userId(userId)
                .items(List.of(itemDTO))
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(orderDTO)
                .when()
                .post("/api/orders")
                .then()
                .statusCode(200)
                .body("userId", equalTo(userId))
                .body("items.size()", equalTo(1))
                .body("totalPrice", equalTo(2000.0f))
                .body("status", equalTo("PENDING"))
                .body("id", notNullValue());
    }

    @Test
    void givenInvalidOrderDTO_whenCreateOrder_thenReturns400() {
        OrderItemDTO itemDTO = new OrderItemDTO("some-product-id", 2);
        OrderDTO orderDTO = OrderDTO.builder()
                .userId(null)
                .items(List.of(itemDTO))
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(orderDTO)
                .when()
                .post("/api/orders")
                .then()
                .statusCode(400);
    }

    @Test
    void givenEmptyItems_whenCreateOrder_thenReturns400() {
        UserDTO userDTO = UserDTO.builder()
                .name("Sara")
                .email("sara@test.com")
                .phoneNumber("+962791234567")
                .build();

        String userId = given()
                .contentType(ContentType.JSON)
                .body(userDTO)
                .when()
                .post("/api/users")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        OrderDTO orderDTO = OrderDTO.builder()
                .userId(userId)
                .items(List.of())
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(orderDTO)
                .when()
                .post("/api/orders")
                .then()
                .statusCode(400);
    }

    @Test
    void givenNonExistingProduct_whenCreateOrder_thenReturns400() {
        UserDTO userDTO = UserDTO.builder()
                .name("Khalid")
                .email("khalid@test.com")
                .phoneNumber("+962791234567")
                .build();

        String userId = given()
                .contentType(ContentType.JSON)
                .body(userDTO)
                .when()
                .post("/api/users")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        OrderItemDTO itemDTO = new OrderItemDTO("non-existing-product-id", 2);
        OrderDTO orderDTO = OrderDTO.builder()
                .userId(userId)
                .items(List.of(itemDTO))
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(orderDTO)
                .when()
                .post("/api/orders")
                .then()
                .statusCode(400);
    }

    @Test
    void givenValidOrderId_whenFindOrderById_thenReturns200AndOrder() {
        UserDTO userDTO = UserDTO.builder()
                .name("Remas")
                .email("remas@test.com")
                .phoneNumber("+962791234567")
                .build();

        String userId = given()
                .contentType(ContentType.JSON)
                .body(userDTO)
                .when()
                .post("/api/users")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        ProductDTO productDTO = ProductDTO.builder()
                .name("Mouse")
                .price(50.0)
                .description("Wireless mouse")
                .build();

        String productId = given()
                .contentType(ContentType.JSON)
                .body(productDTO)
                .when()
                .post("/api/products")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        OrderItemDTO itemDTO = new OrderItemDTO(productId, 1);
        OrderDTO orderDTO = OrderDTO.builder()
                .userId(userId)
                .items(List.of(itemDTO))
                .build();

        String orderId = given()
                .contentType(ContentType.JSON)
                .body(orderDTO)
                .when()
                .post("/api/orders")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        given()
                .when()
                .get("/api/orders/" + orderId)
                .then()
                .statusCode(200)
                .body("id", equalTo(orderId))
                .body("userId", equalTo(userId));
    }

    @Test
    void givenInvalidOrderId_whenFindOrderById_thenReturns400() {
        given()
                .when()
                .get("/api/orders/non-existing-order-id")
                .then()
                .statusCode(400);
    }

    @Test
    void givenValidUserId_whenFindOrderByUserId_thenReturns200AndOrders() {
        UserDTO userDTO = UserDTO.builder()
                .name("Hamza")
                .email("hamza@test.com")
                .phoneNumber("+962791234567")
                .build();

        String userId = given()
                .contentType(ContentType.JSON)
                .body(userDTO)
                .when()
                .post("/api/users")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        ProductDTO productDTO = ProductDTO.builder()
                .name("Keyboard")
                .price(75.0)
                .description("Mechanical keyboard")
                .build();

        String productId = given()
                .contentType(ContentType.JSON)
                .body(productDTO)
                .when()
                .post("/api/products")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        OrderItemDTO itemDTO = new OrderItemDTO(productId, 1);
        OrderDTO orderDTO = OrderDTO.builder()
                .userId(userId)
                .items(List.of(itemDTO))
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(orderDTO)
                .when()
                .post("/api/orders")
                .then()
                .statusCode(200);

        given()
                .when()
                .get("/api/orders/user/" + userId)
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1))
                .body("[0].userId", equalTo(userId));
    }

    @Test
    void givenInvalidUserId_whenFindOrderByUserId_thenReturns400() {
        given()
                .when()
                .get("/api/orders/user/non-existing-user-id")
                .then()
                .statusCode(400);
    }

    @Test
    void givenValidStatus_whenFindOrderByStatus_thenReturns200AndOrders() {
        UserDTO userDTO = UserDTO.builder()
                .name("Test User")
                .email("testuser@test.com")
                .phoneNumber("+962791234567")
                .build();

        String userId = given()
                .contentType(ContentType.JSON)
                .body(userDTO)
                .when()
                .post("/api/users")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        ProductDTO productDTO = ProductDTO.builder()
                .name("Test Product")
                .price(100.0)
                .description("Test description")
                .build();

        String productId = given()
                .contentType(ContentType.JSON)
                .body(productDTO)
                .when()
                .post("/api/products")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        OrderItemDTO itemDTO = new OrderItemDTO(productId, 1);
        OrderDTO orderDTO = OrderDTO.builder()
                .userId(userId)
                .items(List.of(itemDTO))
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(orderDTO)
                .when()
                .post("/api/orders")
                .then()
                .statusCode(200);

        given()
                .when()
                .get("/api/orders/status/PENDING")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    void givenExistingOrder_whenUpdateOrder_thenReturns200AndUpdatedOrder() {
        UserDTO userDTO = UserDTO.builder()
                .name("Update Test")
                .email("updatetest@test.com")
                .phoneNumber("+962791234567")
                .build();

        String userId = given()
                .contentType(ContentType.JSON)
                .body(userDTO)
                .when()
                .post("/api/users")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        ProductDTO productDTO1 = ProductDTO.builder()
                .name("Product 1")
                .price(100.0)
                .description("First product")
                .build();

        String productId1 = given()
                .contentType(ContentType.JSON)
                .body(productDTO1)
                .when()
                .post("/api/products")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        ProductDTO productDTO2 = ProductDTO.builder()
                .name("Product 2")
                .price(200.0)
                .description("Second product")
                .build();

        String productId2 = given()
                .contentType(ContentType.JSON)
                .body(productDTO2)
                .when()
                .post("/api/products")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        OrderItemDTO itemDTO = new OrderItemDTO(productId1, 1);
        OrderDTO orderDTO = OrderDTO.builder()
                .userId(userId)
                .items(List.of(itemDTO))
                .build();

        String orderId = given()
                .contentType(ContentType.JSON)
                .body(orderDTO)
                .when()
                .post("/api/orders")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        OrderItemDTO newItemDTO = new OrderItemDTO(productId2, 2);
        OrderDTO updateDTO = OrderDTO.builder()
                .userId(userId)
                .items(List.of(newItemDTO))
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(updateDTO)
                .when()
                .put("/api/orders/" + orderId)
                .then()
                .statusCode(200)
                .body("id", equalTo(orderId))
                .body("totalPrice", equalTo(400.0f));
    }

    @Test
    void givenExistingOrder_whenDeleteOrder_thenReturns200AndOrderIsDeleted() {
        UserDTO userDTO = UserDTO.builder()
                .name("Delete Test")
                .email("deletetest@test.com")
                .phoneNumber("+962791234567")
                .build();

        String userId = given()
                .contentType(ContentType.JSON)
                .body(userDTO)
                .when()
                .post("/api/users")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        ProductDTO productDTO = ProductDTO.builder()
                .name("Delete Product")
                .price(50.0)
                .description("To be deleted")
                .build();

        String productId = given()
                .contentType(ContentType.JSON)
                .body(productDTO)
                .when()
                .post("/api/products")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        OrderItemDTO itemDTO = new OrderItemDTO(productId, 1);
        OrderDTO orderDTO = OrderDTO.builder()
                .userId(userId)
                .items(List.of(itemDTO))
                .build();

        String orderId = given()
                .contentType(ContentType.JSON)
                .body(orderDTO)
                .when()
                .post("/api/orders")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        given()
                .when()
                .delete("/api/orders/" + orderId)
                .then()
                .statusCode(200);

        given()
                .when()
                .get("/api/orders/" + orderId)
                .then()
                .statusCode(400);
    }

    @Test
    void givenOrders_whenGetAllOrders_thenReturns200AndOrderList() {
        given()
                .when()
                .get("/api/orders")
                .then()
                .statusCode(200)
                .body("$", instanceOf(List.class));
    }
}