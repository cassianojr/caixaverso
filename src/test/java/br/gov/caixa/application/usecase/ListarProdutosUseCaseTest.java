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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class ListarProdutosUseCaseTest {

	@InjectMocks
	ListarProdutosUseCase useCase;

	@Mock
	ProdutoRepository produtoRepository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("Deve retornar lista de produtos quando existirem registros")
	void deveRetornarListaProdutos() {
		List<Produto> mockLista = List.of(
				new Produto(1L, "Prod A", 10.0, 12),
				new Produto(2L, "Prod B", 12.5, 24)
		);
		when(produtoRepository.listarTodos()).thenReturn(mockLista);

		List<Produto> resultado = useCase.executar();

		assertNotNull(resultado);
		assertEquals(2, resultado.size());
		assertEquals("Prod A", resultado.get(0).nome());
		verify(produtoRepository, times(1)).listarTodos();
		verifyNoMoreInteractions(produtoRepository);
	}

	@Test
	@DisplayName("Deve retornar lista vazia quando não houver produtos")
	void deveRetornarListaVazia() {
		when(produtoRepository.listarTodos()).thenReturn(List.of());

		List<Produto> resultado = useCase.executar();

		assertNotNull(resultado);
		assertTrue(resultado.isEmpty());
		verify(produtoRepository, times(1)).listarTodos();
		verifyNoMoreInteractions(produtoRepository);
	}
}
