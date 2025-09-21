package br.gov.caixa.application.usecase;

import br.gov.caixa.domain.model.Produto;
import br.gov.caixa.ports.outbound.ProdutoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CriarProdutoUseCase {

    @Inject
    ProdutoRepository produtoRepository;

    public Produto executar(Produto produto) {
        if (produto.getTaxaJurosAnual() == null || produto.getTaxaJurosAnual() <= 0) {
            throw new IllegalArgumentException("Taxa de juros anual deve ser maior que zero.");
        }

        if (produto.getPrazoMaximoMeses() == null || produto.getPrazoMaximoMeses() <= 0) {
            throw new IllegalArgumentException("Prazo máximo deve ser maior que zero.");
        }

        if (produto.getNome() == null || produto.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório.");
        }

        return produtoRepository.salvar(produto);
    }
}
