package br.gov.caixa.application.usecase;

import br.gov.caixa.application.service.SimulacaoService;
import br.gov.caixa.domain.exception.NegocioException;
import br.gov.caixa.domain.model.MemoriaCalculo;
import br.gov.caixa.domain.model.Produto;
import br.gov.caixa.domain.model.ResultadoSimulacao;
import br.gov.caixa.domain.model.Simulacao;
import br.gov.caixa.ports.outbound.ProdutoRepository;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
public class SimularEmprestimoUseCaseTest {
    @Mock
    SimulacaoService simulacaoService;

    @InjectMocks
    SimularEmprestimoUseCase useCase;

    @Mock
    ProdutoRepository produtoRepository;

    private Produto produto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        produto = new Produto(1L, "Empréstimo Pessoal", 18.0, 24);
    }

    @Test
    @DisplayName("Deve simular empréstimo com sucesso")
    void deveSimularEmprestimoComSucesso() {
        ResultadoSimulacao resultadoMock = new ResultadoSimulacao(
                produto,
                BigDecimal.valueOf(10000.0),
                12,
                BigDecimal.valueOf(0.013978),
                BigDecimal.valueOf(11178.0),
                BigDecimal.valueOf(931.50),
                new ArrayList<>()
        );

        Produto produtoMock = new Produto(1L, "Empréstimo Pessoal", 18.0, 24);

        when(simulacaoService.simular(produtoMock, BigDecimal.valueOf(10000.0), 12))
                .thenReturn(resultadoMock);
        when(produtoRepository.buscarPorId(1L)).thenReturn(java.util.Optional.of(produtoMock));


        Simulacao simulacao = new Simulacao(BigDecimal.valueOf(10000.0), 12);

        ResultadoSimulacao resultado = useCase.simular(simulacao, 1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.produto().id());
        assertEquals(11178.0, resultado.valorTotalComJuros().doubleValue(), 0.001);
        assertEquals(931.50, resultado.parcelaMensal().doubleValue(), 0.001);

        verify(simulacaoService, times(1))
                .simular(produtoMock, BigDecimal.valueOf(10000.0), 12);
    }

    @Test
    @DisplayName("Deve lançar NotFoundException quando produto não existe")
    void deveLancarNotFoundQuandoProdutoNaoExiste() {
        when(produtoRepository.buscarPorId(99L)).thenReturn(java.util.Optional.empty());

        Simulacao simulacao = new Simulacao(BigDecimal.valueOf(5000.0), 6);

        var ex = assertThrows(jakarta.ws.rs.NotFoundException.class, () -> useCase.simular(simulacao, 99L));
        assertTrue(ex.getMessage().contains("Produto não encontrado"));
        verify(produtoRepository).buscarPorId(99L);
        verifyNoInteractions(simulacaoService);
    }

    @Test
    @DisplayName("Deve lançar NegocioException quando prazo excede máximo")
    void deveLancarNegocioExceptionQuandoPrazoExcede() {
        Produto produtoMock = new Produto(2L, "Crédito Consignado", 12.0, 24);
        when(produtoRepository.buscarPorId(2L)).thenReturn(java.util.Optional.of(produtoMock));

        Simulacao simulacao = new Simulacao(BigDecimal.valueOf(2000.0), 25); // 1 a mais

        NegocioException ex = assertThrows(NegocioException.class, () -> useCase.simular(simulacao, 2L));
        assertEquals("Prazo em meses excede o máximo permitido pelo produto", ex.getMessage());
        verify(produtoRepository).buscarPorId(2L);
        verifyNoInteractions(simulacaoService);
    }

    @Test
    @DisplayName("Deve permitir prazo exatamente igual ao máximo")
    void devePermitirPrazoIgualAoMaximo() {
        Produto produtoMock = new Produto(3L, "Financiamento", 10.0, 36);
        when(produtoRepository.buscarPorId(3L)).thenReturn(java.util.Optional.of(produtoMock));

        Simulacao simulacao = new Simulacao(BigDecimal.valueOf(30000.0), 36); // igual ao limite

        ResultadoSimulacao resultadoMock = new ResultadoSimulacao(
                produtoMock,
                simulacao.valorSolicitado(),
                simulacao.prazoMeses(),
                BigDecimal.valueOf(0.01),
                BigDecimal.valueOf(33300.0),
                BigDecimal.valueOf(925.0),
                new ArrayList<>()
        );

        when(simulacaoService.simular(produtoMock, simulacao.valorSolicitado(), simulacao.prazoMeses()))
                .thenReturn(resultadoMock);

        ResultadoSimulacao out = useCase.simular(simulacao, 3L);

        assertNotNull(out);
        assertEquals(36, out.prazoMeses());
        verify(simulacaoService).simular(produtoMock, BigDecimal.valueOf(30000.0), 36);
    }

    @Test
    @DisplayName("Não deve alterar estado do produto passado ao service")
    void naoDeveAlterarEstadoProduto() {
        Produto produtoMock = new Produto(5L, "Produto X", 15.0, 12);
        when(produtoRepository.buscarPorId(5L)).thenReturn(java.util.Optional.of(produtoMock));
        Simulacao simulacao = new Simulacao(BigDecimal.valueOf(1000.0), 10);

        ResultadoSimulacao resultadoMock = new ResultadoSimulacao(
                produtoMock,
                simulacao.valorSolicitado(),
                simulacao.prazoMeses(),
                null,
                null,
                null,
                null
        );


        when(simulacaoService.simular(produtoMock, BigDecimal.valueOf(1000.0), 10)).thenReturn(resultadoMock);

        useCase.simular(simulacao, 5L);

        assertEquals(15.0, produtoMock.taxaJurosAnual()); // garantindo que nada alterou
        verify(simulacaoService).simular(produtoMock, BigDecimal.valueOf(1000.0), 10);
    }
}
