package br.gov.caixa.application.usecase;

import br.gov.caixa.domain.model.Produto;
import br.gov.caixa.ports.outbound.ProdutoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class BuscarProdutoUseCase {

    @Inject
    ProdutoRepository produtoRepository;

    public Optional<Produto> executar(Long id) {
        return produtoRepository.buscarPorId(id);
    }
}
