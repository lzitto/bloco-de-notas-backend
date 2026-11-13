package com.leozito.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import io.restassured.http.ContentType; // import novo
import java.util.UUID;     

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
    
    @Test
    void postNotaComTituloEmBrancoDeveRetornar400() {
        given()
          .contentType(ContentType.JSON)                          // envia JSON
          .body("{\"userId\":\"" + UUID.randomUUID()             // userId ok
              + "\",\"title\":\"\", \"content\":\"x\"}")           // title VAZIO → @NotBlank
          .when().post("/api/notes")
          .then()
             .statusCode(400);                                    // validação barrou
    }
}