package br.gov.caixa.application.usecase;

import br.gov.caixa.ports.outbound.ProdutoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class RemoverProdutoUseCase {
    @Inject
    ProdutoRepository produtoRepository;

    public void executar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id do produto não pode ser nulo");
        }
        produtoRepository.remover(id);
    }
}
