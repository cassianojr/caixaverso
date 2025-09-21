package br.gov.caixa.adapters.inbound.rest;

import br.gov.caixa.domain.model.Produto;
import br.gov.caixa.domain.model.Simulacao;
import br.gov.caixa.ports.outbound.ProdutoRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class SimulacaoResourceIntegrationTest {

	@Inject
	ProdutoRepository produtoRepository;

	private Long produtoId;

	@BeforeEach
	void setupProduto() {
		Produto salvo = produtoRepository.salvar(new Produto(null, "Emprestimo Teste", 18.0, 36));
		produtoId = salvo.id();
	}

	@AfterEach
	void cleanupProduto() {
		if (produtoId != null) {
			produtoRepository.remover(produtoId);
		}
	}

	@Test
	@DisplayName("POST /simulacoes/{idProduto} deve retornar 200 e dados da simulacao")
	void simularOk() {
		Simulacao body = new Simulacao(BigDecimal.valueOf(5000), 12);

		given()
				.contentType(ContentType.JSON)
				.body(body)
				.when()
				.post("/simulacoes/{id}", produtoId)
				.then()
				.statusCode(200)
				.body("produto.id", equalTo(produtoId.intValue()))
				.body("valorSolicitado", equalTo(5000.00f))
				.body("prazoMeses", equalTo(12))
				.body("memoriaCalculo", notNullValue())
				.body("parcelaMensal", greaterThan(0f));
	}

	@Test
	@DisplayName("POST /simulacoes/{idProduto} com produto inexistente deve retornar 404 e JSON de erro")
	void simularProdutoInexistente() {
		Simulacao body = new Simulacao(BigDecimal.valueOf(1000), 6);

		given()
				.contentType(ContentType.JSON)
				.body(body)
				.when()
				.post("/simulacoes/{id}", 999999)
				.then()
				.statusCode(404)
				.contentType(containsString("application/json"))
				.body("mensagem", containsString("Produto não encontrado"));
	}

	@Test
	@DisplayName("POST /simulacoes/{idProduto} com prazo acima do máximo deve retornar 400")
	void simularPrazoMaiorQueMaximo() {
		Simulacao body = new Simulacao(BigDecimal.valueOf(2000), 999); // maior que 36

		given()
				.contentType(ContentType.JSON)
				.body(body)
				.when()
				.post("/simulacoes/{id}", produtoId)
				.then()
				.statusCode(400)
				.body("mensagem", containsString("Prazo em meses excede"));
	}
}
