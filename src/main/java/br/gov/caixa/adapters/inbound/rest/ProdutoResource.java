package br.gov.caixa.adapters.inbound.rest;

import br.gov.caixa.application.usecase.CriarProdutoUseCase;
import br.gov.caixa.domain.model.Produto;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/produtos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProdutoResource {

    @Inject
    CriarProdutoUseCase criarProdutoUseCase;

    @POST
    public Response criarProduto(Produto produto){
        Produto produtoCriado = criarProdutoUseCase.executar(produto);
        return Response.status(Response.Status.CREATED).entity(produtoCriado).build();
    }
}
