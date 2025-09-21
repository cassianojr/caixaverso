package br.gov.caixa.domain.model;

import java.math.BigDecimal;

public record Simulacao(
    BigDecimal valorSolicitado,
    int prazoMeses
) {}
