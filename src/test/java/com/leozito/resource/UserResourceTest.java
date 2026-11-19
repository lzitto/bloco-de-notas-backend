package com.leozito.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasKey;

@QuarkusTest
class UserResourceTest {

    private String criarUsuario() {
        String email = "user-" + UUID.randomUUID() + "@mail.com";
        return given()
          .contentType(ContentType.JSON)
          .body("{\"email\":\"" + email + "\",\"password\":\"senha123\"}")
          .when().post("/api/users")
          .then().statusCode(201)
          .extract().path("id");
    }

    @Test
    void listarUsuariosDeveRetornar200ComLista() {
        criarUsuario();

        given()
          .when().get("/api/users")
          .then()
             .statusCode(200)
             .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    void criarUsuarioValidoDeveRetornar201SemExporSenha() {
        String email = "novo-" + UUID.randomUUID() + "@mail.com";

        given()
          .contentType(ContentType.JSON)
          .body("{\"email\":\"" + email + "\",\"password\":\"senha123\"}")
          .when().post("/api/users")
          .then()
             .statusCode(201)
             .body("email", is(email))
             .body("id", notNullValue())
             .body(not(hasKey("password")));
    }

    @Test
    void criarUsuarioComEmailInvalidoDeveRetornar400() {
        given()
          .contentType(ContentType.JSON)
          .body("{\"email\":\"nao-e-email\",\"password\":\"senha123\"}")
          .when().post("/api/users")
          .then()
             .statusCode(400);
    }

    @Test
    void criarUsuarioComCamposVaziosDeveRetornar400() {
        given()
          .contentType(ContentType.JSON)
          .body("{\"email\":\"\",\"password\":\"\"}")
          .when().post("/api/users")
          .then()
             .statusCode(400);
    }

    @Test
    void criarUsuarioComEmailDuplicadoDeveRetornar400() {
        String email = "dup-" + UUID.randomUUID() + "@mail.com";

        given()
          .contentType(ContentType.JSON)
          .body("{\"email\":\"" + email + "\",\"password\":\"senha123\"}")
          .when().post("/api/users")
          .then().statusCode(201);

        given()
          .contentType(ContentType.JSON)
          .body("{\"email\":\"" + email + "\",\"password\":\"outra\"}")
          .when().post("/api/users")
          .then()
             .statusCode(400);
    }

    @Test
    void getUsuarioExistenteDeveRetornar200() {
        String id = criarUsuario();

        given()
          .when().get("/api/users/{id}", id)
          .then()
             .statusCode(200)
             .body("id", is(id));
    }

    @Test
    void getUsuarioInexistenteDeveRetornar404() {
        given()
          .when().get("/api/users/{id}", UUID.randomUUID())
          .then()
             .statusCode(404);
    }

    @Test
    void putUsuarioExistenteDeveRetornar200Atualizado() {
        String id = criarUsuario();
        String email = "atualizado-" + UUID.randomUUID() + "@mail.com";

        given()
          .contentType(ContentType.JSON)
          .body("{\"email\":\"" + email + "\",\"password\":\"nova123\"}")
          .when().put("/api/users/{id}", id)
          .then()
             .statusCode(200)
             .body("email", is(email))
             .body(not(hasKey("password")));
    }

    @Test
    void putUsuarioInexistenteDeveRetornar404() {
        given()
          .contentType(ContentType.JSON)
          .body("{\"email\":\"x-" + UUID.randomUUID() + "@mail.com\",\"password\":\"s\"}")
          .when().put("/api/users/{id}", UUID.randomUUID())
          .then()
             .statusCode(404);
    }

    @Test
    void putUsuarioComEmailDeOutroUsuarioDeveRetornar400() {
        String idA = criarUsuario();
        String idB = criarUsuario();

        String emailDeA = given()
          .when().get("/api/users/{id}", idA)
          .then().statusCode(200)
          .extract().path("email");

        given()
          .contentType(ContentType.JSON)
          .body("{\"email\":\"" + emailDeA + "\",\"password\":\"s\"}")
          .when().put("/api/users/{id}", idB)
          .then()
             .statusCode(400);
    }

    @Test
    void deleteUsuarioExistenteDeveRetornar204() {
        String id = criarUsuario();

        given()
          .when().delete("/api/users/{id}", id)
          .then()
             .statusCode(204);
    }

    @Test
    void deleteUsuarioInexistenteDeveRetornar404() {
        given()
          .when().delete("/api/users/{id}", UUID.randomUUID())
          .then()
             .statusCode(404);
    }

    @Test
    void senhaNaoDeveSerExpostaNoJsonDaResposta() {
        String id = criarUsuario();

        String body = given()
          .when().get("/api/users/{id}", id)
          .then().statusCode(200)
          .extract().asString();

        org.junit.jupiter.api.Assertions.assertFalse(body.contains("password"));
        org.junit.jupiter.api.Assertions.assertFalse(body.contains("senha123"));
    }
}