package br.gov.caixa.ports.inbound;

import br.gov.caixa.domain.model.ResultadoSimulacao;
import br.gov.caixa.domain.model.Simulacao;

public interface SimulacaoPort {
    ResultadoSimulacao simular(Simulacao simulacao, Long idProduto);
}
