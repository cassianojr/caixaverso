package br.gov.caixa.application.usecase;

import br.gov.caixa.domain.model.Produto;
import br.gov.caixa.ports.outbound.ProdutoRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class CriarProdutoUseCaseTest {

	@InjectMocks
	CriarProdutoUseCase useCase;

	@Mock
	ProdutoRepository produtoRepository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("Deve criar produto válido")
	void deveCriarProdutoValido() {
		Produto entrada = new Produto(null, "Produto Teste", 12.5, 24);
		Produto salvo = new Produto(1L, "Produto Teste", 12.5, 24);
		when(produtoRepository.salvar(entrada)).thenReturn(salvo);

		Produto resultado = useCase.executar(entrada);

		assertNotNull(resultado);
		assertEquals(1L, resultado.getId());
		verify(produtoRepository, times(1)).salvar(entrada);
	}

	@Nested
	class Validacoes {

		@Test
		@DisplayName("Deve falhar quando taxa de juros for nula")
		void deveFalharTaxaNula() {
			Produto p = new Produto(null, "Produto", null, 12);
			IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> useCase.executar(p));
			assertTrue(ex.getMessage().contains("Taxa de juros anual"));
			verifyNoInteractions(produtoRepository);
		}

		@Test
		@DisplayName("Deve falhar quando taxa de juros for zero ou negativa")
		void deveFalharTaxaZeroOuNegativa() {
			Produto p = new Produto(null, "Produto", 0.0, 12);
			IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> useCase.executar(p));
			assertTrue(ex.getMessage().contains("Taxa de juros anual"));
			verifyNoInteractions(produtoRepository);
		}

		@Test
		@DisplayName("Deve falhar quando prazo máximo for nulo")
		void deveFalharPrazoNulo() {
			Produto p = new Produto(null, "Produto", 10.0, null);
			IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> useCase.executar(p));
			assertTrue(ex.getMessage().contains("Prazo máximo"));
			verifyNoMoreInteractions(produtoRepository);
		}

		@Test
		@DisplayName("Deve falhar quando prazo máximo for zero")
		void deveFalharPrazoZero() {
			Produto p = new Produto(null, "Produto", 10.0, 0);
			IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> useCase.executar(p));
			assertTrue(ex.getMessage().contains("Prazo máximo"));
			verifyNoInteractions(produtoRepository);
		}

		@Test
		@DisplayName("Deve falhar quando nome for nulo ou em branco")
		void deveFalharNomeInvalido() {
			Produto p1 = new Produto(null, null, 10.0, 12);
			IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> useCase.executar(p1));
			assertTrue(ex1.getMessage().contains("Nome do produto"));

			Produto p2 = new Produto(null, "   ", 10.0, 12);
			IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> useCase.executar(p2));
			assertTrue(ex2.getMessage().contains("Nome do produto"));

			verifyNoInteractions(produtoRepository);
		}
	}
}
