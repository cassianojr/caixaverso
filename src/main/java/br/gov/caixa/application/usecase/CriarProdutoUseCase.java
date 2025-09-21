package br.gov.caixa.application.usecase;

import br.gov.caixa.domain.model.Produto;
import br.gov.caixa.ports.outbound.ProdutoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CriarProdutoUseCase {

    private final ProdutoRepository produtoRepository;

    @Inject
    public CriarProdutoUseCase(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Produto executar(Produto produto) {
        if (produto.taxaJurosAnual() == null || produto.taxaJurosAnual() <= 0) {
            throw new IllegalArgumentException("Taxa de juros anual deve ser maior que zero.");
        }

        if (produto.prazoMaximoMeses() == null || produto.prazoMaximoMeses() <= 0) {
            throw new IllegalArgumentException("Prazo máximo deve ser maior que zero.");
        }

        if (produto.nome() == null || produto.nome().isBlank()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório.");
        }

        return produtoRepository.salvar(produto);
    }
}
