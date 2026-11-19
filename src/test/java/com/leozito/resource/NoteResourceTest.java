package com.leozito.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
class NoteResourceTest {

    private String criarUsuario() {
        String email = "nota-" + UUID.randomUUID() + "@mail.com";
        String id = given()
          .contentType(ContentType.JSON)
          .body("{\"email\":\"" + email + "\",\"password\":\"senha123\"}")
          .when().post("/api/users")
          .then().statusCode(201)
          .extract().path("id");
        return id;
    }

    private String criarNota(String userId) {
        String id = given()
          .contentType(ContentType.JSON)
          .body("{\"userId\":\"" + userId + "\",\"title\":\"T\",\"content\":\"C\"}")
          .when().post("/api/notes")
          .then().statusCode(201)
          .extract().path("id");
        return id;
    }

    @Test
    void postNotaValidaDeveRetornar201ComId() {
        String userId = criarUsuario();

        given()
          .contentType(ContentType.JSON)
          .body("{\"userId\":\"" + userId + "\",\"title\":\"Olá\",\"content\":\"Mundo\"}")
          .when().post("/api/notes")
          .then()
             .statusCode(201)
             .body("title", is("Olá"))
             .body("id", notNullValue());
    }

    @Test
    void getNotaExistenteDeveRetornar200() {
        String userId = criarUsuario();
        String noteId = criarNota(userId);

        given()
          .when().get("/api/notes/{id}", noteId)
          .then()
             .statusCode(200)
             .body("id", is(noteId));
    }

    @Test
    void getNotaInexistenteDeveRetornar404() {
        given()
          .when().get("/api/notes/{id}", UUID.randomUUID())
          .then()
             .statusCode(404);
    }

    @Test
    void getNotasPorUsuarioDeveRetornarApenasAsDesteUsuario() {
        String userId = criarUsuario();
        String outraUser = criarUsuario();
        criarNota(userId);
        criarNota(userId);
        criarNota(outraUser);

        given()
          .when().get("/api/notes/user/{userId}", userId)
          .then()
             .statusCode(200)
             .body("size()", is(2))
             .body("userId", everyItem(is(userId)));
    }

    @Test
    void getNotasDeUsuarioSemNotasDeveRetornar200ComListaVazia() {
        String userId = criarUsuario();

        given()
          .when().get("/api/notes/user/{userId}", userId)
          .then()
             .statusCode(200)
             .body(is("[]"));
    }

    @Test
    void putNotaExistenteDeveRetornar200Atualizada() {
        String userId = criarUsuario();
        String noteId = criarNota(userId);

        given()
          .contentType(ContentType.JSON)
          .body("{\"userId\":\"" + userId + "\",\"title\":\"Novo\",\"content\":\"Atualizado\"}")
          .when().put("/api/notes/{id}", noteId)
          .then()
             .statusCode(200)
             .body("title", is("Novo"))
             .body("content", is("Atualizado"));
    }

    @Test
    void putNotaInexistenteDeveRetornar404() {
        String userId = criarUsuario();

        given()
          .contentType(ContentType.JSON)
          .body("{\"userId\":\"" + userId + "\",\"title\":\"T\",\"content\":\"C\"}")
          .when().put("/api/notes/{id}", UUID.randomUUID())
          .then()
             .statusCode(404);
    }

    @Test
    void deleteNotaExistenteDeveRetornar204() {
        String userId = criarUsuario();
        String noteId = criarNota(userId);

        given()
          .when().delete("/api/notes/{id}", noteId)
          .then()
             .statusCode(204);
    }

    @Test
    void deleteNotaInexistenteDeveRetornar404() {
        given()
          .when().delete("/api/notes/{id}", UUID.randomUUID())
          .then()
             .statusCode(404);
    }

    @Test
    void postNotaSemUserIdDeveRetornar400() {
        given()
          .contentType(ContentType.JSON)
          .body("{\"title\":\"T\",\"content\":\"C\"}")
          .when().post("/api/notes")
          .then()
             .statusCode(400);
    }

    @Test
    void postNotaComTituloEmBrancoDeveRetornar400() {
        String userId = criarUsuario();

        given()
          .contentType(ContentType.JSON)
          .body("{\"userId\":\"" + userId + "\",\"title\":\"\",\"content\":\"x\"}")
          .when().post("/api/notes")
          .then()
             .statusCode(400);
    }

    @Test
    void postNotaComUsuarioInexistenteDeveRetornar400() {
        given()
          .contentType(ContentType.JSON)
          .body("{\"userId\":\"" + UUID.randomUUID() + "\",\"title\":\"T\",\"content\":\"C\"}")
          .when().post("/api/notes")
          .then()
             .statusCode(400);
    }
}