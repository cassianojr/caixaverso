package br.gov.caixa.adapters.inbound.rest;

import br.gov.caixa.application.usecase.*;
import br.gov.caixa.domain.model.Produto;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class ProdutoResourceUnitTest {

    private final CriarProdutoUseCase criar = mock(CriarProdutoUseCase.class);
    private final BuscarProdutoUseCase buscar = mock(BuscarProdutoUseCase.class);
    private final ListarProdutosUseCase listar = mock(ListarProdutosUseCase.class);
    private final AtualizarProdutoUseCase atualizar = mock(AtualizarProdutoUseCase.class);
    private final RemoverProdutoUseCase remover = mock(RemoverProdutoUseCase.class);

    private final ProdutoResource resource = new ProdutoResource(criar, buscar, listar, atualizar, remover);

    @Test
    @DisplayName("GET /produtos/{id} deve retornar 200 e body quando encontrado")
    void buscarProdutoPorIdEncontrado() {
        Long id = 1L;
        Produto esperado = new Produto(id, "Crediario", 15.0, 48);
        when(buscar.executar(id)).thenReturn(Optional.of(esperado));

        Response resp = resource.buscarProdutoPorId(id);

        assertEquals(200, resp.getStatus());
        assertSame(esperado, resp.getEntity());
        verify(buscar, times(1)).executar(id);
        verifyNoMoreInteractions(buscar);
    }

    @Test
    @DisplayName("GET /produtos/{id} deve retornar 404 quando não encontrado")
    void buscarProdutoPorIdNaoEncontrado() {
        Long id = 2L;
        when(buscar.executar(id)).thenReturn(Optional.empty());

        Response resp = resource.buscarProdutoPorId(id);

        assertEquals(404, resp.getStatus());
        assertNull(resp.getEntity());
        verify(buscar, times(1)).executar(id);
        verifyNoMoreInteractions(buscar);
    }

    @Test
    @DisplayName("GET /produtos deve retornar lista e 200")
    void listarProdutosOk() {
        List<Produto> lista = List.of(
                new Produto(1L, "Prod A", 10.0, 12),
                new Produto(2L, "Prod B", 11.5, 24)
        );
        when(listar.executar()).thenReturn(lista);

        Response resp = resource.listarProdutos();

        assertEquals(200, resp.getStatus());
        assertSame(lista, resp.getEntity());
        verify(listar, times(1)).executar();
        verifyNoMoreInteractions(listar);
    }

    @Test
    @DisplayName("POST /produtos deve criar e retornar 201")
    void criarProdutoOk() {
        Produto entrada = new Produto(null, "Novo", 9.5, 36);
        Produto criado = new Produto(10L, entrada.nome(), entrada.taxaJurosAnual(), entrada.prazoMaximoMeses());
        when(criar.executar(entrada)).thenReturn(criado);

        Response resp = resource.criarProduto(entrada);

        assertEquals(201, resp.getStatus());
        assertSame(criado, resp.getEntity());
        verify(criar, times(1)).executar(entrada);
        verifyNoMoreInteractions(criar);
    }

    @Test
    @DisplayName("PUT /produtos/{id} deve atualizar e retornar 200")
    void atualizarProdutoOk() {
        Long id = 5L;
        Produto body = new Produto(null, "Alterado", 12.0, 60); // id ignorado no body
        Produto esperadoEnviado = new Produto(id, body.nome(), body.taxaJurosAnual(), body.prazoMaximoMeses());
        Produto atualizadoRetorno = new Produto(id, body.nome(), 12.0, 60);
        when(atualizar.executar(any(Produto.class))).thenReturn(atualizadoRetorno);

        Response resp = resource.atualizarProduto(id, body);

        assertEquals(200, resp.getStatus());
        assertSame(atualizadoRetorno, resp.getEntity());

        ArgumentCaptor<Produto> captor = ArgumentCaptor.forClass(Produto.class);
        verify(atualizar, times(1)).executar(captor.capture());
        Produto enviado = captor.getValue();
        assertEquals(esperadoEnviado.id(), enviado.id());
        assertEquals(esperadoEnviado.nome(), enviado.nome());
        assertEquals(esperadoEnviado.taxaJurosAnual(), enviado.taxaJurosAnual());
        assertEquals(esperadoEnviado.prazoMaximoMeses(), enviado.prazoMaximoMeses());
        verifyNoMoreInteractions(atualizar);
    }

    @Test
    @DisplayName("DELETE /produtos/{id} deve remover e retornar 204")
    void deletarProdutoOk() {
        Long id = 7L;

        Response resp = resource.deletarProduto(id);

        assertEquals(204, resp.getStatus());
        assertNull(resp.getEntity());
        verify(remover, times(1)).executar(id);
        verifyNoMoreInteractions(remover);
    }
}
