package br.gov.caixa.adapters.outbound.persistence;

import br.gov.caixa.domain.model.Produto;
import br.gov.caixa.ports.outbound.ProdutoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProdutoRepositoryImpl implements ProdutoRepository {

    @Override
    @Transactional
    public Produto salvar(Produto produto) {
        ProdutoEntity entity = toEntity(produto);
        if(entity.getId() == null){
            entity.persist();
        }else{
            entity = ProdutoEntity.findById(entity.getId());
            entity.setNome(produto.getNome());
            entity.setTaxaJurosAnual(produto.getTaxaJurosAnual());
            entity.setPrazoMaximoMeses(produto.getPrazoMaximoMeses());
        }
        return toDomain(entity);
    }

    @Override
    public Optional<Produto> buscarPorId(Long id) {
        ProdutoEntity entity = ProdutoEntity.findById(id);
        return entity != null ? Optional.of(toDomain(entity)) : Optional.empty();
    }

    @Override
    public List<Produto> listarTodos() {
        return ProdutoEntity.listAll()
                .stream()
                .map(e -> toDomain((ProdutoEntity) e))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void remover(Long id) {
        ProdutoEntity.deleteById(id);
    }

    private ProdutoEntity toEntity(Produto produto) {
        ProdutoEntity entity = new ProdutoEntity();
        entity.setId(produto.getId());
        entity.setNome(produto.getNome());
        entity.setTaxaJurosAnual(produto.getTaxaJurosAnual());
        entity.setPrazoMaximoMeses(produto.getPrazoMaximoMeses());
        return entity;
    }

    private Produto toDomain(ProdutoEntity entity) {
        return new Produto(
                entity.getId(),
                entity.getNome(),
                entity.getTaxaJurosAnual(),
                entity.getPrazoMaximoMeses()
        );
    }
}
