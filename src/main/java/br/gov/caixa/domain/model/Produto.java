package br.gov.caixa.domain.model;

public record Produto(
        Long id,
        String nome,
        Double taxaJurosAnual,
        Integer prazoMaximoMeses
) {
    public Produto withId(Long id) {
        return new Produto(id, this.nome, this.taxaJurosAnual, this.prazoMaximoMeses);
    }
}