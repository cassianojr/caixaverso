package br.gov.caixa.domain.model;

import java.math.BigDecimal;
import java.util.List;

public record ResultadoSimulacao(
    Produto produto,
    BigDecimal valorSolicitado,
    int prazoMeses,
    BigDecimal taxaJurosEfetivaMensal,
    BigDecimal valorTotalComJuros,
    BigDecimal parcelaMensal,
    List<MemoriaCalculo> memoriaCalculo
) {}
