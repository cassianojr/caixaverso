package br.gov.caixa.domain.model;

import java.math.BigDecimal;

public class Simulacao {
    private BigDecimal valorSolicitado;
    private int prazoMeses;

    public Simulacao(BigDecimal valorSolicitado, int prazoMeses) {
        this.valorSolicitado = valorSolicitado;
        this.prazoMeses = prazoMeses;
    }

    public BigDecimal getValorSolicitado() {
        return valorSolicitado;
    }

    public void setValorSolicitado(BigDecimal valorSolicitado) {
        this.valorSolicitado = valorSolicitado;
    }

    public int getPrazoMeses() {
        return prazoMeses;
    }

    public void setPrazoMeses(int prazoMeses) {
        this.prazoMeses = prazoMeses;
    }
}
