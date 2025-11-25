package com.example.shop.controller;

import com.example.shop.domain.dto.UserDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    void givenValidUserDTO_whenCreateUser_thenReturns200AndUser() {
        UserDTO dto = UserDTO.builder()
                .name("Ahmad")
                .email("ahmad@test.com")
                .phoneNumber("+962791234567")
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/api/users")
                .then()
                .statusCode(200)
                .body("name", equalTo("Ahmad"))
                .body("email", equalTo("ahmad@test.com"))
                .body("phoneNumber", equalTo("+962791234567"))
                .body("id", notNullValue());
    }

    @Test
    void givenShortName_whenCreateUser_thenReturns400() {
        UserDTO dto = UserDTO.builder()
                .name("Ab")
                .email("test@test.com")
                .phoneNumber("+962791234567")
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/api/users")
                .then()
                .statusCode(400);
    }

    @Test
    void givenInvalidEmail_whenCreateUser_thenReturns400() {
        UserDTO dto = UserDTO.builder()
                .name("Ahmad")
                .email("invalid-email")
                .phoneNumber("+962791234567")
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/api/users")
                .then()
                .statusCode(400);
    }

    @Test
    void givenInvalidPhoneNumber_whenCreateUser_thenReturns400() {
        UserDTO dto = UserDTO.builder()
                .name("Ahmad")
                .email("ahmad@test.com")
                .phoneNumber("1234567890")
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/api/users")
                .then()
                .statusCode(400);
    }

    @Test
    void givenExistingUserId_whenGetUserById_thenReturns200AndUser() {
        UserDTO createDTO = UserDTO.builder()
                .name("Sara")
                .email("sara@test.com")
                .phoneNumber("+962791234567")
                .build();

        String userId = given()
                .contentType(ContentType.JSON)
                .body(createDTO)
                .when()
                .post("/api/users")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        given()
                .when()
                .get("/api/users/" + userId)
                .then()
                .statusCode(200)
                .body("id", equalTo(userId))
                .body("name", equalTo("Sara"))
                .body("email", equalTo("sara@test.com"))
                .body("phoneNumber", equalTo("+962791234567"));
    }

    @Test
    void givenNonExistingUserId_whenGetUserById_thenReturns400() {
        given()
                .when()
                .get("/api/users/non-existing-id-123")
                .then()
                .statusCode(400);
    }

    @Test
    void givenExistingUser_whenUpdateUser_thenReturns200AndUpdatedUser() {
        UserDTO createDTO = UserDTO.builder()
                .name("OldName")
                .email("old@test.com")
                .phoneNumber("+962791234567")
                .build();

        String userId = given()
                .contentType(ContentType.JSON)
                .body(createDTO)
                .when()
                .post("/api/users")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        UserDTO updateDTO = UserDTO.builder()
                .name("NewName")
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(updateDTO)
                .when()
                .put("/api/users/" + userId)
                .then()
                .statusCode(200)
                .body("id", equalTo(userId))
                .body("name", equalTo("NewName"))
                .body("email", equalTo("old@test.com"))
                .body("phoneNumber", equalTo("+962791234567"));
    }

    @Test
    void givenExistingUser_whenDeleteUser_thenReturns200AndUserIsDeleted() {
        UserDTO createDTO = UserDTO.builder()
                .name("ToDelete")
                .email("delete@test.com")
                .phoneNumber("+962791234567")
                .build();

        String userId = given()
                .contentType(ContentType.JSON)
                .body(createDTO)
                .when()
                .post("/api/users")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        given()
                .when()
                .delete("/api/users/" + userId)
                .then()
                .statusCode(200);

        given()
                .when()
                .get("/api/users/" + userId)
                .then()
                .statusCode(400);
    }

    @Test
    void givenValidUsers_whenGetAllUsers_thenReturns200AndUserList() {
        UserDTO user1 = UserDTO.builder()
                .name("User1")
                .email("user1@test.com")
                .phoneNumber("+962791234567")
                .build();

        UserDTO user2 = UserDTO.builder()
                .name("User2")
                .email("user2@test.com")
                .phoneNumber("+962791234568")
                .build();

        given().contentType(ContentType.JSON).body(user1).when().post("/api/users");
        given().contentType(ContentType.JSON).body(user2).when().post("/api/users");

        given()
                .when()
                .get("/api/users")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(2));
    }

    @Test
    void givenDuplicateEmail_whenCreateUser_thenReturns400() {
        UserDTO user1 = UserDTO.builder()
                .name("User1")
                .email("same@test.com")
                .phoneNumber("+962791234567")
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(user1)
                .when()
                .post("/api/users")
                .then()
                .statusCode(200);

        UserDTO user2 = UserDTO.builder()
                .name("User2")
                .email("same@test.com")
                .phoneNumber("+962791234568")
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(user2)
                .when()
                .post("/api/users")
                .then()
                .statusCode(400);
    }
}