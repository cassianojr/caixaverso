package br.gov.caixa.application.usecase;


import br.gov.caixa.domain.model.Produto;
import br.gov.caixa.ports.outbound.ProdutoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ListarProdutosUseCase {
    private final ProdutoRepository produtoRepository;

    @Inject
    public ListarProdutosUseCase(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public List<Produto> executar() {
        return produtoRepository.listarTodos();
    }
}
