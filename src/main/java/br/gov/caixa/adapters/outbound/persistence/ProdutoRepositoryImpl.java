package br.gov.caixa.adapters.outbound.persistence;

import br.gov.caixa.domain.model.Produto;
import br.gov.caixa.ports.outbound.ProdutoRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ProdutoRepositoryImpl implements ProdutoRepository, PanacheRepository<ProdutoEntity> {

    @Override
    @Transactional
    public Produto salvar(Produto produto) {
        ProdutoEntity entity = toEntity(produto);
        if(entity.getId() == null){
            entity.persist();
        }else{
            entity = findById(entity.getId());
            entity.setNome(produto.nome());
            entity.setTaxaJurosAnual(produto.taxaJurosAnual());
            entity.setPrazoMaximoMeses(produto.prazoMaximoMeses());
        }
        return toDomain(entity);
    }

    @Override
    public Optional<Produto> buscarPorId(Long id) {
        ProdutoEntity entity = findById(id);
        return entity != null ? Optional.of(toDomain(entity)) : Optional.empty();
    }

    @Override
    public List<Produto> listarTodos() {
        return listAll()
                .stream()
                .map(this::toDomain).toList();
    }

    @Override
    @Transactional
    public void remover(Long id) {
        deleteById(id);
    }

    private ProdutoEntity toEntity(Produto produto) {
        ProdutoEntity entity = new ProdutoEntity();
        entity.setId(produto.id());
        entity.setNome(produto.nome());
        entity.setTaxaJurosAnual(produto.taxaJurosAnual());
        entity.setPrazoMaximoMeses(produto.prazoMaximoMeses());
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
