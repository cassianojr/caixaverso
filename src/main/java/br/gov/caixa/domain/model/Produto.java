package br.gov.caixa.domain.model;

public record Produto(
        Long id,
        String nome,
        Double taxaJurosAnual,
        Integer prazoMaximoMeses
) {}