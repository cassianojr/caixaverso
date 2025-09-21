package br.gov.caixa.domain.model;

import java.math.BigDecimal;

public record MemoriaCalculo(
    int mes,
    BigDecimal saldoDevedorInicial,
    BigDecimal juros,
    BigDecimal amortizacao,
    BigDecimal saldoDevedorFinal
){}
