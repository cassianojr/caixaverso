package br.gov.caixa.adapters.inbound.rest;

import br.gov.caixa.domain.model.Simulacao;
import br.gov.caixa.ports.inbound.SimulacaoPort;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/simulacoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SimulacaoResource {

    private final SimulacaoPort simulacaoPort;

    public SimulacaoResource(SimulacaoPort simulacaoPort) {
        this.simulacaoPort = simulacaoPort;
    }

    @POST
    @Path("/{idProduto}")
    public Response simularProduto(@PathParam("idProduto") Long idProduto, Simulacao simulacao){
        return Response.ok(simulacaoPort.simular(simulacao, idProduto)).build();
    }
}
