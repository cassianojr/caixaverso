package br.gov.caixa.ports.outbound;

import br.gov.caixa.domain.model.Produto;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository {
    Produto salvar(Produto produto);
    Optional<Produto> buscarPorId(Long id);
    List<Produto> listarTodos();
    void remover(Long id);
}
