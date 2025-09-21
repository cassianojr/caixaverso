package br.gov.caixa.adapters.inbound.rest;

import br.gov.caixa.domain.model.MemoriaCalculo;
import br.gov.caixa.domain.model.Produto;
import br.gov.caixa.domain.model.ResultadoSimulacao;
import br.gov.caixa.domain.model.Simulacao;
import br.gov.caixa.ports.inbound.SimulacaoPort;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class SimulacaoResourceUnitTest {

	@Test
	@DisplayName("simularProduto deve delegar ao port e retornar 200 com body")
	void simularProdutoOk() {
		// Arrange
		SimulacaoPort port = mock(SimulacaoPort.class);
		SimulacaoResource resource = new SimulacaoResource(port);

		Long idProduto = 10L;
		Simulacao simulacao = new Simulacao(BigDecimal.valueOf(5000), 24);

		ResultadoSimulacao esperado = new ResultadoSimulacao(
				new Produto(idProduto, "Emprestimo", 18.0, 60),
				simulacao.valorSolicitado(),
				simulacao.prazoMeses(),
				BigDecimal.valueOf(0.015),
				BigDecimal.valueOf(6000),
				BigDecimal.valueOf(250),
				Collections.<MemoriaCalculo>emptyList()
		);
		when(port.simular(simulacao, idProduto)).thenReturn(esperado);

		// Act
		Response response = resource.simularProduto(idProduto, simulacao);

		// Assert
		assertNotNull(response, "Response não deve ser nulo");
		assertEquals(200, response.getStatus(), "Status HTTP incorreto");
		assertSame(esperado, response.getEntity(), "Body diferente do retornado pelo port");

		ArgumentCaptor<Simulacao> simulacaoCaptor = ArgumentCaptor.forClass(Simulacao.class);
		ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
		verify(port, times(1)).simular(simulacaoCaptor.capture(), idCaptor.capture());
		assertEquals(simulacao, simulacaoCaptor.getValue(), "Simulacao enviada incorreta");
		assertEquals(idProduto, idCaptor.getValue(), "IdProduto enviado incorreto");
		verifyNoMoreInteractions(port);
	}

	@Test
	@DisplayName("simularProduto deve propagar exceção do port")
	void simularProdutoPropagaExcecao() {
		SimulacaoPort port = mock(SimulacaoPort.class);
		SimulacaoResource resource = new SimulacaoResource(port);
		Long idProduto = 99L;
		Simulacao simulacao = new Simulacao(BigDecimal.TEN, 6);
		RuntimeException erro = new RuntimeException("falha negocio");
		when(port.simular(simulacao, idProduto)).thenThrow(erro);

		RuntimeException lancada = assertThrows(RuntimeException.class, () -> resource.simularProduto(idProduto, simulacao));
		assertSame(erro, lancada, "Deveria propagar a mesma exceção");
		verify(port, times(1)).simular(simulacao, idProduto);
		verifyNoMoreInteractions(port);
	}
}
