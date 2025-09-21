package br.gov.caixa.application.service;

import br.gov.caixa.domain.exception.NegocioException;
import br.gov.caixa.domain.model.MemoriaCalculo;
import br.gov.caixa.domain.model.Produto;
import br.gov.caixa.domain.model.ResultadoSimulacao;
import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class SimulacaoServiceTest {

	private final SimulacaoService service = new SimulacaoService();

	@Test
	@DisplayName("calcularTaxaMensal deve converter taxa anual percentual para efetiva mensal")
	void calcularTaxaMensalOk() {
		BigDecimal taxaMensal = service.calcularTaxaMensal(BigDecimal.valueOf(18.0));
		// Valor esperado aproximado: (1+0.18)^(1/12)-1 ≈ 0.01388843
		assertEquals(0.0138884, taxaMensal.doubleValue(), 1e-7, "Taxa mensal incorreta");
	}

	@Test
	@DisplayName("simular deve gerar memoria completa e saldo final zero")
	void simularOk() {
		Produto produto = new Produto(1L, "Emprestimo", 18.0, 60);
		BigDecimal valor = BigDecimal.valueOf(10_000.00);
		int prazo = 12;

		ResultadoSimulacao resultado = service.simular(produto, valor, prazo);

		assertNotNull(resultado);
		assertEquals(prazo, resultado.memoriaCalculo().size(), "Quantidade de parcelas incorreta");
		assertEquals(valor.setScale(2, RoundingMode.HALF_EVEN), resultado.valorSolicitado());
		assertTrue(resultado.parcelaMensal().compareTo(BigDecimal.ZERO) > 0, "Parcela deve ser positiva");

		// Última linha da memória deve ter saldo final 0.00
		MemoriaCalculo ultima = resultado.memoriaCalculo().get(prazo - 1);
		assertEquals(BigDecimal.ZERO.setScale(2), ultima.saldoDevedorFinal(), "Saldo final deve ser zero");

		// Soma das amortizações deve ser aproximadamente o valor solicitado (diferenças de arredondamento toleradas em até 1 centavo)
		BigDecimal somaAmort = resultado.memoriaCalculo().stream()
				.map(MemoriaCalculo::amortizacao)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	BigDecimal diff = somaAmort.subtract(valor).abs();
	assertTrue(diff.compareTo(BigDecimal.valueOf(0.02)) <= 0, "Soma das amortizações difere além do tolerado: diff=" + diff);
	}

	@Test
	@DisplayName("simular deve lançar NegocioException quando produto é null")
	void simularProdutoNull() {
		assertThrows(NegocioException.class, () -> service.simular(null, BigDecimal.TEN, 10));
	}

	@Test
	@DisplayName("simular deve lançar NegocioException quando valorSolicitado <= 0")
	void simularValorInvalido() {
		Produto produto = new Produto(1L, "Emprestimo", 18.0, 60);
		assertAll(
				() -> assertThrows(NegocioException.class, () -> service.simular(produto, BigDecimal.ZERO, 10)),
				() -> assertThrows(NegocioException.class, () -> service.simular(produto, BigDecimal.valueOf(-1), 10))
		);
	}

	@Test
	@DisplayName("simular deve lançar NegocioException quando valorSolicitado for null")
	void simularValorNull() {
		Produto produto = new Produto(1L, "Emprestimo", 18.0, 60);
		assertAll(
				() -> assertThrows(NegocioException.class, () -> service.simular(produto, null, 10))
		);
	}

	@Test
	@DisplayName("simular deve lançar NegocioException quando prazoMeses <= 0")
	void simularPprazoInvalido() {
		Produto produto = new Produto(1L, "Emprestimo", 18.0, 60);
		assertAll(
				() -> assertThrows(NegocioException.class, () -> service.simular(produto, BigDecimal.TEN, 0)),
				() -> assertThrows(NegocioException.class, () -> service.simular(produto, BigDecimal.TEN, -5))
		);
	}

	@Test
	@DisplayName("simular com taxa zero deve lançar NegocioException")
	void simularTaxaZero() {
		Produto produto = new Produto(1L, "Credito Sem Juros", 0.0, 12);
		assertThrows(NegocioException.class, () -> service.simular(produto, BigDecimal.valueOf(1000), 12));
	}

	@Test
	@DisplayName("calcular taxa mensal com valor null deve lançar NegocioException")
	void calcularTaxaMensalNull() {
		assertThrows(NegocioException.class, () -> service.calcularTaxaMensal(null));
	}
}
