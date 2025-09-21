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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class BuscarProdutoUseCaseTest {

	@InjectMocks
	BuscarProdutoUseCase useCase;

	@Mock
	ProdutoRepository produtoRepository;

	@BeforeEach
	void init() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("Deve retornar produto quando existir")
	void deveRetornarProdutoExistente() {
		Produto produto = new Produto(1L, "Produto X", 10.0, 12);
		when(produtoRepository.buscarPorId(1L)).thenReturn(Optional.of(produto));

		Optional<Produto> resultado = useCase.executar(1L);

		assertTrue(resultado.isPresent());
		assertEquals(produto, resultado.get());
		verify(produtoRepository, times(1)).buscarPorId(1L);
		verifyNoMoreInteractions(produtoRepository);
	}

	@Test
	@DisplayName("Deve retornar Optional.empty quando produto não existir")
	void deveRetornarVazioQuandoNaoExiste() {
		when(produtoRepository.buscarPorId(2L)).thenReturn(Optional.empty());

		Optional<Produto> resultado = useCase.executar(2L);

		assertTrue(resultado.isEmpty());
		verify(produtoRepository).buscarPorId(2L);
		verifyNoMoreInteractions(produtoRepository);
	}

	@Test
	@DisplayName("Deve propagar exceção lançada pelo repositório")
	void devePropagarExcecaoRepositorio() {
		RuntimeException erro = new RuntimeException("Falha de acesso ao banco");
		when(produtoRepository.buscarPorId(3L)).thenThrow(erro);

		RuntimeException ex = assertThrows(RuntimeException.class, () -> useCase.executar(3L));
		assertEquals("Falha de acesso ao banco", ex.getMessage());
		verify(produtoRepository).buscarPorId(3L);
		verifyNoMoreInteractions(produtoRepository);
	}
}
