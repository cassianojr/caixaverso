package br.gov.caixa.adapters.inbound.rest;

import br.gov.caixa.application.usecase.*;
import br.gov.caixa.domain.model.Produto;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/produtos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProdutoResource {

    private final CriarProdutoUseCase criarProdutoUseCase;

    private final BuscarProdutoUseCase buscarProdutoUseCase;

    private final ListarProdutosUseCase listarProdutosUseCase;

    private final AtualizarProdutoUseCase atualizarProdutoUseCase;

    private final RemoverProdutoUseCase removerProdutoUseCase;

    @Inject
    public ProdutoResource(CriarProdutoUseCase criarProdutoUseCase, BuscarProdutoUseCase buscarProdutoUseCase, ListarProdutosUseCase listarProdutosUseCase, AtualizarProdutoUseCase atualizarProdutoUseCase, RemoverProdutoUseCase removerProdutoUseCase) {
        this.criarProdutoUseCase = criarProdutoUseCase;
        this.buscarProdutoUseCase = buscarProdutoUseCase;
        this.listarProdutosUseCase = listarProdutosUseCase;
        this.atualizarProdutoUseCase = atualizarProdutoUseCase;
        this.removerProdutoUseCase = removerProdutoUseCase;
    }

    @GET
    @Path("/{id}")
    public Response buscarProdutoPorId(@PathParam("id") Long id){
        return buscarProdutoUseCase.executar(id)
                .map(produto -> Response.ok(produto).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    public Response listarProdutos(){
        List<Produto> produtos = listarProdutosUseCase.executar();
        return Response.ok(produtos).build();
    }

    @POST
    public Response criarProduto(Produto produto){
        Produto produtoCriado = criarProdutoUseCase.executar(produto);
        return Response.status(Response.Status.CREATED).entity(produtoCriado).build();
    }

    @PUT
    @Path("/{id}")
    public Response atualizarProduto(@PathParam("id") Long id, Produto produto){
        produto.withId(id);
        Produto produtoAtualizado = atualizarProdutoUseCase.executar(produto);
        return Response.ok(produtoAtualizado).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletarProduto(@PathParam("id") Long id){
        removerProdutoUseCase.executar(id);
        return Response.noContent().build();
    }

}
