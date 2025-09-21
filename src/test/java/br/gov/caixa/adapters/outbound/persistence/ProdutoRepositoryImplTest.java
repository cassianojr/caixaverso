package br.gov.caixa.adapters.outbound.persistence;

import br.gov.caixa.domain.model.Produto;
import br.gov.caixa.ports.outbound.ProdutoRepository;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ProdutoRepositoryImplTest {

	@InjectMocks
	ProdutoRepositoryImpl repository;

	@BeforeEach
	@TestTransaction
	void limparBase() {
		ProdutoEntity.deleteAll();
	}

	@BeforeEach
	void init() {
		MockitoAnnotations.openMocks(this);
	}


	private Produto novoProduto() {
		return new Produto(null, "Produto Teste", 12.5, 24);
	}

	@Test
	@DisplayName("Deve salvar novo produto e gerar ID")
	@TestTransaction
	void deveSalvarNovoProduto() {
		Produto salvo = repository.salvar(novoProduto());
		assertNotNull(salvo.id(), "ID deveria ser gerado");
		assertEquals("Produto Teste", salvo.nome());
	}

	@Test
	@DisplayName("Deve atualizar produto existente")
	@TestTransaction
	void deveAtualizarProduto() {
		Produto salvo = repository.salvar(novoProduto());
		Produto atualizado = new Produto(salvo.id(), "Produto Alterado", 15.0, 36);
		Produto resultado = repository.salvar(atualizado);
		assertEquals(salvo.id(), resultado.id());
		assertEquals("Produto Alterado", resultado.nome());
		assertEquals(15.0, resultado.taxaJurosAnual());
		assertEquals(36, resultado.prazoMaximoMeses());
	}

	@Test
	@DisplayName("Deve buscar produto por ID existente")
	@TestTransaction
	void deveBuscarPorIdExistente() {
		Produto salvo = repository.salvar(novoProduto());
		Optional<Produto> opt = repository.buscarPorId(salvo.id());
		assertTrue(opt.isPresent());
		assertEquals(salvo.id(), opt.get().id());
	}

	@Test
	@DisplayName("Deve retornar vazio ao buscar ID inexistente")
	@TestTransaction
	void deveRetornarVazioQuandoIdNaoExiste() {
		Optional<Produto> opt = repository.buscarPorId(9999L);
		assertTrue(opt.isEmpty());
	}

	@Test
	@DisplayName("Deve listar todos os produtos")
	@TestTransaction
	void deveListarTodos() {
		repository.salvar(novoProduto());
		repository.salvar(new Produto(null, "Outro", 10.0, 12));
		List<Produto> lista = repository.listarTodos();
		assertEquals(2, lista.size());
	}

	@Test
	@DisplayName("Deve remover produto por ID")
	@TestTransaction
	void deveRemover() {
		Produto salvo = repository.salvar(novoProduto());
		repository.remover(salvo.id());
		assertTrue(repository.buscarPorId(salvo.id()).isEmpty());
	}
}
