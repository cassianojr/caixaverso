package br.gov.caixa.application.usecase;


import br.gov.caixa.domain.model.Produto;
import br.gov.caixa.ports.outbound.ProdutoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ListarProdutosUseCase {
    @Inject
    ProdutoRepository produtoRepository;

    public List<Produto> executar() {
        return produtoRepository.listarTodos();
    }
}
