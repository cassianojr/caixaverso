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
    private final ProdutoRepository produtoRepository;

    private final SimulacaoService simulacaoService;

    @Inject
    public SimularEmprestimoUseCase(ProdutoRepository produtoRepository, SimulacaoService simulacaoService) {
        this.produtoRepository = produtoRepository;
        this.simulacaoService = simulacaoService;
    }

    @Override
    public ResultadoSimulacao simular(Simulacao simulacao, Long idProduto) {
        Produto produtoExistente = produtoRepository.buscarPorId(idProduto)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado com ID: " + idProduto));


        if(simulacao.prazoMeses() > produtoExistente.prazoMaximoMeses()){
            throw new NegocioException(Response.Status.BAD_REQUEST.getStatusCode(),"Prazo em meses excede o máximo permitido pelo produto");
        }

        BigDecimal valorSolicitado = simulacao.valorSolicitado();
        int prazoMeses = simulacao.prazoMeses();
        return simulacaoService.simular(produtoExistente, valorSolicitado, prazoMeses);
    }

}
