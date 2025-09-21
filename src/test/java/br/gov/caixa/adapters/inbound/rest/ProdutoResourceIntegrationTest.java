package br.gov.caixa.adapters.inbound.rest;

import br.gov.caixa.domain.model.Produto;
import br.gov.caixa.ports.outbound.ProdutoRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class ProdutoResourceIntegrationTest {

    @Inject
    ProdutoRepository produtoRepository;

    Long criadoId;

    @AfterEach
    void cleanup() {
        if (criadoId != null) {
            try { produtoRepository.remover(criadoId); } catch (Exception ignored) {}
            criadoId = null;
        }
    }

    @Test
    @DisplayName("POST /produtos deve criar produto e retornar 201")
    void criarProduto() {
        Produto body = new Produto(null, "Prod Integracao", 13.5, 72);

        criadoId = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/produtos")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("nome", equalTo("Prod Integracao"))
                .body("taxaJurosAnual", equalTo(13.5f))
                .body("prazoMaximoMeses", equalTo(72))
                .extract().jsonPath().getLong("id");
    }

    @Test
    @DisplayName("GET /produtos/{id} com inexistente deve retornar 404")
    void buscarInexistente404() {
        given()
                .when()
                .get("/produtos/{id}", 999999)
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Fluxo completo CRUD produto")
    void fluxoCrudCompleto() {
        // Create
        Produto body = new Produto(null, "Fluxo CRUD", 20.0, 48);
        criadoId = given().contentType(ContentType.JSON).body(body)
                .when().post("/produtos")
                .then().statusCode(201)
                .extract().jsonPath().getLong("id");

        // Read (GET by id)
        given().when().get("/produtos/{id}", criadoId)
                .then().statusCode(200)
                .body("id", equalTo(criadoId.intValue()))
                .body("nome", equalTo("Fluxo CRUD"));

        // List
        given().when().get("/produtos")
                .then().statusCode(200)
                .body("id", hasItem(criadoId.intValue()));

        // Update
        Produto update = new Produto(null, "Fluxo CRUD Alterado", 22.0, 60);
        given().contentType(ContentType.JSON).body(update)
                .when().put("/produtos/{id}", criadoId)
                .then().statusCode(200)
                .body("id", equalTo(criadoId.intValue()))
                .body("nome", equalTo("Fluxo CRUD Alterado"))
                .body("taxaJurosAnual", equalTo(22.0f))
                .body("prazoMaximoMeses", equalTo(60));

        // Delete
        given().when().delete("/produtos/{id}", criadoId)
                .then().statusCode(204);

        // Confirm 404 after delete
        given().when().get("/produtos/{id}", criadoId)
                .then().statusCode(404);

        criadoId = null; // já removido
    }
}
