package com.leozito.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class NoteResourceTest {

    @Test
    void getNotasComBancoVazioDeveRetornar200ComListaVazia() {
        // Assume o armazenamento em memória vazio. Se outros testes
        // passarem a criar notas, este teste precisa limpar o estado antes.
        given()
          .when().get("/api/notes")
          .then()
             .statusCode(200)
             .body(is("[]"));
    }
}