package br.gov.caixa.domain.model;

import java.math.BigDecimal;

public class MemoriaCalculo {
    private int mes;
    private BigDecimal saldoDevedorInicial;
    private BigDecimal juros;
    private BigDecimal amortizacao;
    private BigDecimal saldoDevedorFinal;

    public MemoriaCalculo() {
    }

    public MemoriaCalculo(int mes, BigDecimal saldoDevedorInicial, BigDecimal juros, BigDecimal amortizacao, BigDecimal saldoDevedorFinal) {
        this.mes = mes;
        this.saldoDevedorInicial = saldoDevedorInicial;
        this.juros = juros;
        this.amortizacao = amortizacao;
        this.saldoDevedorFinal = saldoDevedorFinal;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public BigDecimal getSaldoDevedorInicial() {
        return saldoDevedorInicial;
    }

    public void setSaldoDevedorInicial(BigDecimal saldoDevedorInicial) {
        this.saldoDevedorInicial = saldoDevedorInicial;
    }

    public BigDecimal getJuros() {
        return juros;
    }

    public void setJuros(BigDecimal juros) {
        this.juros = juros;
    }

    public BigDecimal getAmortizacao() {
        return amortizacao;
    }

    public void setAmortizacao(BigDecimal amortizacao) {
        this.amortizacao = amortizacao;
    }

    public BigDecimal getSaldoDevedorFinal() {
        return saldoDevedorFinal;
    }

    public void setSaldoDevedorFinal(BigDecimal saldoDevedorFinal) {
        this.saldoDevedorFinal = saldoDevedorFinal;
    }
}
