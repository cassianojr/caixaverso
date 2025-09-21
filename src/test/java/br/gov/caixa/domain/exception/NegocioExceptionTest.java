package br.gov.caixa.domain.exception;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class NegocioExceptionTest {

    @Test
    @DisplayName("Deve armazenar codigo e mensagem corretamente")
    void deveArmazenarCodigoEMensagem() {
        NegocioException ex = new NegocioException(123, "Falha de negócio");
        assertEquals(123, ex.getCodigo());
        assertEquals("Falha de negócio", ex.getMessage());
    }

    @Test
    @DisplayName("Deve ser subtipo de RuntimeException")
    void deveSerSubtipoRuntimeException() {
        NegocioException ex = new NegocioException(1, "msg");
        assertInstanceOf(RuntimeException.class, ex);
    }

    @Test
    @DisplayName("Mensagem não deve ser nula")
    void mensagemNaoDeveSerNula() {
        NegocioException ex = new NegocioException(2, "Teste");
        assertNotNull(ex.getMessage());
    }

    @Test
    @DisplayName("Suporta codigos grandes")
    void suportaCodigosGrandes() {
        int codigo = Integer.MAX_VALUE;
        NegocioException ex = new NegocioException(codigo, "Limite");
        assertEquals(Integer.MAX_VALUE, ex.getCodigo());
    }
}
