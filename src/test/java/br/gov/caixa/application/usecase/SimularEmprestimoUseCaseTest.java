package br.gov.caixa.application.usecase;

import br.gov.caixa.application.service.SimulacaoService;
import br.gov.caixa.domain.exception.NegocioException;
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
        ResultadoSimulacao resultadoMock = new ResultadoSimulacao();
        resultadoMock.setProduto(produto);
        resultadoMock.setValorSolicitado(BigDecimal.valueOf(10000.0));
        resultadoMock.setPrazoMeses(12);
        resultadoMock.setTaxaJurosEfetivaMensal(BigDecimal.valueOf(0.013978));
        resultadoMock.setValorTotalComJuros(BigDecimal.valueOf(11178.0));
        resultadoMock.setParcelaMensal(BigDecimal.valueOf(931.50));

        Produto produtoMock = new Produto(1L, "Empréstimo Pessoal", 18.0, 24);

        when(simulacaoService.simular(produtoMock, BigDecimal.valueOf(10000.0), 12))
                .thenReturn(resultadoMock);
        when(produtoRepository.buscarPorId(1L)).thenReturn(java.util.Optional.of(produtoMock));


        Simulacao simulacao = new Simulacao(BigDecimal.valueOf(10000.0), 12);

        ResultadoSimulacao resultado = useCase.simular(simulacao, 1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getProduto().getId());
        assertEquals(11178.0, resultado.getValorTotalComJuros().doubleValue(), 0.001);
        assertEquals(931.50, resultado.getParcelaMensal().doubleValue(), 0.001);

        verify(simulacaoService, times(1))
                .simular(produtoMock, BigDecimal.valueOf(10000.0), 12);
    }


}
