package br.gov.caixa.application.usecase;

import br.gov.caixa.ports.outbound.ProdutoRepository;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class RemoverProdutoUseCaseTest {

    @InjectMocks
    RemoverProdutoUseCase useCase;

    @Mock
    ProdutoRepository produtoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve remover produto existente chamando repositório uma vez")
    void deveRemoverProduto() {
        Long id = 10L;

        // ação
        useCase.executar(id);

        // verificação
        verify(produtoRepository, times(1)).remover(id);
        verifyNoMoreInteractions(produtoRepository);
    }

    @Nested
    class Excecoes {
        @Test
        @DisplayName("Deve propagar exceção do repositório ao remover")
        void devePropagarExcecaoRepositorio() {
            Long id = 99L;
            RuntimeException erro = new RuntimeException("Falha ao remover");
            doThrow(erro).when(produtoRepository).remover(id);

            RuntimeException ex = assertThrows(RuntimeException.class, () -> useCase.executar(id));
            assertEquals("Falha ao remover", ex.getMessage());
            verify(produtoRepository, times(1)).remover(id);
            verifyNoMoreInteractions(produtoRepository);
        }

        @Test
        @DisplayName("Deve falhar quando id for nulo")
        void deveFalharIdNulo() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> useCase.executar(null));
            assertTrue(ex.getMessage().contains("Id do produto"));
            verifyNoInteractions(produtoRepository);
        }
    }
}
