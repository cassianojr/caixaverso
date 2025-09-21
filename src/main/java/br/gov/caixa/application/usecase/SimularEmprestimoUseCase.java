package br.gov.caixa.application.usecase;

import br.gov.caixa.application.service.SimulacaoService;
import br.gov.caixa.domain.exception.NegocioException;
import br.gov.caixa.domain.model.Produto;
import br.gov.caixa.domain.model.ResultadoSimulacao;
import br.gov.caixa.domain.model.Simulacao;
import br.gov.caixa.ports.inbound.SimulacaoPort;
import br.gov.caixa.ports.outbound.ProdutoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;

@ApplicationScoped
public class SimularEmprestimoUseCase implements SimulacaoPort {
    @Inject
    ProdutoRepository produtoRepository;

    @Inject
    SimulacaoService simulacaoService;

    @Override
    public ResultadoSimulacao simular(Simulacao simulacao, Long idProduto) {
        Produto produtoExistente = produtoRepository.buscarPorId(idProduto)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado com ID: " + idProduto));


        if(simulacao.getPrazoMeses() > produtoExistente.prazoMaximoMeses()){
            throw new NegocioException(Response.Status.BAD_REQUEST.getStatusCode(),"Prazo em meses excede o máximo permitido pelo produto");
        }

        BigDecimal valorSolicitado = simulacao.getValorSolicitado();
        int prazoMeses = simulacao.getPrazoMeses();
        return simulacaoService.simular(produtoExistente, valorSolicitado, prazoMeses);
    }

}
