package br.gov.caixa.application.usecase;

import br.gov.caixa.ports.outbound.ProdutoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class RemoverProdutoUseCase {
    private final ProdutoRepository produtoRepository;

    @Inject
    public RemoverProdutoUseCase(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public void executar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id do produto não pode ser nulo");
        }
        produtoRepository.remover(id);
    }
}
