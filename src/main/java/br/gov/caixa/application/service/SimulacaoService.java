package br.gov.caixa.application.service;

import br.gov.caixa.domain.exception.NegocioException;
import br.gov.caixa.domain.model.MemoriaCalculo;
import br.gov.caixa.domain.model.Produto;
import br.gov.caixa.domain.model.ResultadoSimulacao;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class SimulacaoService {

    private static final MathContext MC = new MathContext(20, RoundingMode.HALF_EVEN);
    private static final int MONEY_SCALE = 2;

    public BigDecimal calcularTaxaMensal(BigDecimal taxaAnualPercent) {
        if (taxaAnualPercent == null) {
            throw new NegocioException(Response.Status.BAD_REQUEST.getStatusCode(), "taxaAnualPercent é obrigatório");
        }
        double anualDecimal = taxaAnualPercent.divide(BigDecimal.valueOf(100), MC).doubleValue();
        double taxaMensal = Math.pow(1.0 + anualDecimal, 1.0 / 12.0) - 1.0;
        return BigDecimal.valueOf(taxaMensal).round(new MathContext(12, RoundingMode.HALF_EVEN));
    }

    public ResultadoSimulacao simular(Produto produto, BigDecimal valorSolicitado, int prazoMeses) {
        validarSimulacao(produto, valorSolicitado, prazoMeses);

        BigDecimal taxaMensal = calcularTaxaMensal(BigDecimal.valueOf(produto.taxaJurosAnual()));
        BigDecimal taxaMensalExibida = taxaMensal.setScale(10, RoundingMode.HALF_EVEN);

        BigDecimal parcela = calcularParcela(valorSolicitado, prazoMeses, taxaMensal);
        BigDecimal parcelaExibida = parcela.setScale(MONEY_SCALE, RoundingMode.HALF_EVEN);

        List<MemoriaCalculo> memoria = new ArrayList<>(prazoMeses);

        BigDecimal somaParcelas = BigDecimal.ZERO;

        somaParcelas = calcularPrestacoes(prazoMeses, valorSolicitado, taxaMensal, parcela, memoria, somaParcelas);

        BigDecimal valorTotalComJuros = somaParcelas.setScale(MONEY_SCALE, RoundingMode.HALF_EVEN);

        return new ResultadoSimulacao(
                produto,
                valorSolicitado.setScale(MONEY_SCALE, RoundingMode.HALF_EVEN),
                prazoMeses,
                taxaMensalExibida,
                valorTotalComJuros,
                parcelaExibida,
                memoria
        );
    }

    private static BigDecimal calcularPrestacoes(int prazoMeses, BigDecimal saldoAtual, BigDecimal taxaMensal, BigDecimal parcelaPrecisa, List<MemoriaCalculo> memoria, BigDecimal somaParcelasExibidas) {
        for (int mes = 1; mes <= prazoMeses; mes++) {
            BigDecimal jurosPreciso = saldoAtual.multiply(taxaMensal, MC);
            BigDecimal amortizacaoPrecisa = parcelaPrecisa.subtract(jurosPreciso, MC);

            BigDecimal parcelaMesPrecisa = parcelaPrecisa;
            BigDecimal saldoFinalPreciso = saldoAtual.subtract(amortizacaoPrecisa, MC);

            if (mes == prazoMeses) {
                amortizacaoPrecisa = saldoAtual;
                parcelaMesPrecisa = jurosPreciso.add(amortizacaoPrecisa, MC);
                saldoFinalPreciso = BigDecimal.ZERO;
            }

            BigDecimal saldoInicialExibido = saldoAtual.setScale(MONEY_SCALE, RoundingMode.HALF_EVEN);
            BigDecimal jurosExibido = jurosPreciso.setScale(MONEY_SCALE, RoundingMode.HALF_EVEN);
            BigDecimal amortizacaoExibido = amortizacaoPrecisa.setScale(MONEY_SCALE, RoundingMode.HALF_EVEN);
            BigDecimal parcelaExibidaMes = parcelaMesPrecisa.setScale(MONEY_SCALE, RoundingMode.HALF_EVEN);
            BigDecimal saldoFinalExibido = saldoFinalPreciso.setScale(MONEY_SCALE, RoundingMode.HALF_EVEN);

            memoria.add(new MemoriaCalculo(
                    mes,
                    saldoInicialExibido,
                    jurosExibido,
                    amortizacaoExibido,
                    saldoFinalExibido));

            somaParcelasExibidas = somaParcelasExibidas.add(parcelaExibidaMes);
            saldoAtual = saldoFinalPreciso;
        }
        return somaParcelasExibidas;
    }

    private static void validarSimulacao(Produto produto, BigDecimal valorSolicitado, int prazoMeses) {
        if (produto == null) throw new NegocioException(Response.Status.BAD_REQUEST.getStatusCode(), "produto é obrigatório");

        if (valorSolicitado == null || valorSolicitado.compareTo(BigDecimal.ZERO) <= 0)
            throw new NegocioException(Response.Status.BAD_REQUEST.getStatusCode(), "valorSolicitado deve ser > 0");

        if (prazoMeses <= 0) throw new NegocioException(Response.Status.BAD_REQUEST.getStatusCode(),"prazoMeses deve ser > 0");
    }

    private static BigDecimal calcularParcela(BigDecimal valorSolicitado, int prazoMeses, BigDecimal taxaMensal) {

        BigDecimal fator = BigDecimal.ONE.add(taxaMensal, MC);
        BigDecimal potencia = fator.pow(prazoMeses, MC);
        BigDecimal denominador = BigDecimal.ONE.subtract(BigDecimal.ONE.divide(potencia, MC), MC);
        if (denominador.compareTo(BigDecimal.ZERO) == 0) {
            throw new NegocioException(Response.Status.BAD_REQUEST.getStatusCode(), "Denominador zero na fórmula Price");
        }
        return valorSolicitado.multiply(taxaMensal, MC).divide(denominador, MC);
    }
}
