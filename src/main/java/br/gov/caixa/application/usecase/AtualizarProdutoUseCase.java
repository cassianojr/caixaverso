package br.gov.caixa.application.usecase;

import br.gov.caixa.domain.model.Produto;
import br.gov.caixa.ports.outbound.ProdutoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AtualizarProdutoUseCase {

    private final ProdutoRepository produtoRepository;

    @Inject
    public AtualizarProdutoUseCase(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Produto executar(Produto produto) {
        return produtoRepository.salvar(produto);
    }
}
