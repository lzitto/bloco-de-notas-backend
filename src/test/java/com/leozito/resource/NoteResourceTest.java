package com.leozito.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class NoteResourceTest {

    @Test
    void getNotasComBancoVazioDeveRetornar200ComListaVazia() {
        given()
          .when().get("/api/notes")
          .then()
             .statusCode(200)
             .body(is("[]"));
    }

    @Test
    void postNotaComTituloEmBrancoDeveRetornar400() {
        given()
          .contentType(ContentType.JSON)
          .body("{\"userId\":\"" + UUID.randomUUID()
              + "\",\"title\":\"\", \"content\":\"x\"}")
          .when().post("/api/notes")
          .then()
             .statusCode(400);
    }

    @Test
    void postNotaComUsuarioInexistenteDeveRetornar400() {
        given()
          .contentType(ContentType.JSON)
          .body("{\"userId\":\"" + UUID.randomUUID()
              + "\",\"title\":\"T\", \"content\":\"C\"}")
          .when().post("/api/notes")
          .then()
             .statusCode(400);
    }
}