package br.gov.caixa.application.usecase;

import br.gov.caixa.domain.model.Produto;
import br.gov.caixa.ports.outbound.ProdutoRepository;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class AtualizarProdutoUseCaseTest {

	@InjectMocks
	AtualizarProdutoUseCase useCase;

	@Mock
	ProdutoRepository produtoRepository;

	@BeforeEach
	void init() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("Deve atualizar produto existente com sucesso")
	void deveAtualizarProduto() {
		Produto atualizado = new Produto(10L, "Produto Atualizado", 11.0, 18);
		when(produtoRepository.salvar(atualizado)).thenReturn(atualizado);

		Produto resultado = useCase.executar(atualizado);

		assertNotNull(resultado);
		assertEquals(10L, resultado.id());
		assertEquals("Produto Atualizado", resultado.nome());
		assertEquals(11.0, resultado.taxaJurosAnual());
		assertEquals(18, resultado.prazoMaximoMeses());
		verify(produtoRepository, times(1)).salvar(atualizado);
		verifyNoMoreInteractions(produtoRepository);
	}

	@Test
	@DisplayName("Deve retornar null quando repositório retornar null")
	void deveRetornarNullQuandoRepositorioNull() {
		Produto p = new Produto(5L, "Produto", 9.0, 10);
		when(produtoRepository.salvar(p)).thenReturn(null);

		Produto resultado = useCase.executar(p);

		assertNull(resultado);
		verify(produtoRepository).salvar(p);
		verifyNoMoreInteractions(produtoRepository);
	}

	@Test
	@DisplayName("Deve propagar exceção do repositório")
	void devePropagarExcecaoRepositorio() {
		Produto p = new Produto(7L, "Produto", 8.0, 8);
		RuntimeException erro = new RuntimeException("Erro de persistência");
		when(produtoRepository.salvar(p)).thenThrow(erro);

		RuntimeException ex = assertThrows(RuntimeException.class, () -> useCase.executar(p));
		assertEquals("Erro de persistência", ex.getMessage());
		verify(produtoRepository).salvar(p);
		verifyNoMoreInteractions(produtoRepository);
	}

	@Test
	@DisplayName("Deve passar exatamente a mesma instância ao repositório")
	void devePassarMesmaInstancia() {
		Produto p = new Produto(3L, "Produto", 7.0, 6);
		when(produtoRepository.salvar(any())).thenAnswer(invocation -> invocation.getArgument(0));

		Produto resultado = useCase.executar(p);

		assertSame(p, resultado);
		verify(produtoRepository).salvar(p);
		verifyNoMoreInteractions(produtoRepository);
	}
}
