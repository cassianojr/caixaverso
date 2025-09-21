package br.gov.caixa.domain.model;

import java.math.BigDecimal;
import java.util.List;

public class ResultadoSimulacao {
    private Produto produto;
    private BigDecimal valorSolicitado;
    private int prazoMeses;
    private BigDecimal taxaJurosEfetivaMensal;
    private BigDecimal valorTotalComJuros;
    private BigDecimal parcelaMensal;
    private List<MemoriaCalculo> memoriaCalculo;

    public ResultadoSimulacao(Produto produto, BigDecimal valorSolicitado, int prazoMeses, BigDecimal taxaJurosEfetivaMensal, BigDecimal valorTotalComJuros, BigDecimal parcelaMensal, List<MemoriaCalculo> memoriaCalculo) {
        this.produto = produto;
        this.valorSolicitado = valorSolicitado;
        this.prazoMeses = prazoMeses;
        this.taxaJurosEfetivaMensal = taxaJurosEfetivaMensal;
        this.parcelaMensal = parcelaMensal;
        this.memoriaCalculo = memoriaCalculo;
        this.valorTotalComJuros = valorTotalComJuros;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
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

    public BigDecimal getTaxaJurosEfetivaMensal() {
        return taxaJurosEfetivaMensal;
    }

    public void setTaxaJurosEfetivaMensal(BigDecimal taxaJurosEfetivaMensal) {
        this.taxaJurosEfetivaMensal = taxaJurosEfetivaMensal;
    }

    public BigDecimal getParcelaMensal() {
        return parcelaMensal;
    }

    public void setParcelaMensal(BigDecimal parcelaMensal) {
        this.parcelaMensal = parcelaMensal;
    }

    public List<MemoriaCalculo> getMemoriaCalculo() {
        return memoriaCalculo;
    }

    public void setMemoriaCalculo(List<MemoriaCalculo> memoriaCalculo) {
        this.memoriaCalculo = memoriaCalculo;
    }

    public BigDecimal getValorTotalComJuros() {
        return valorTotalComJuros;
    }

    public void setValorTotalComJuros(BigDecimal valorTotalComJuros) {
        this.valorTotalComJuros = valorTotalComJuros;
    }

}
