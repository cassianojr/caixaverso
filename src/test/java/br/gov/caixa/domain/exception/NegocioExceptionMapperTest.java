package br.gov.caixa.domain.exception;

import br.gov.caixa.adapters.inbound.rest.ErrorResponse;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class NegocioExceptionMapperTest {

    private final NegocioExceptionMapper mapper = new NegocioExceptionMapper();

    @Test
    @DisplayName("Deve mapear NegocioException para Response com status e mensagem corretos")
    void deveMapearExceptionComStatusMensagem() {
        NegocioException ex = new NegocioException(422, "Operação inválida");

        Response response = mapper.toResponse(ex);

        assertEquals(422, response.getStatus());
        assertNotNull(response.getEntity());
        assertInstanceOf(ErrorResponse.class, response.getEntity());
        ErrorResponse body = (ErrorResponse) response.getEntity();
        assertEquals("Operação inválida", body.getMensagem());
        assertNotNull(body.getTimestamp());
    }

    @Test
    @DisplayName("Deve usar o código informado na exception (404)")
    void deveUsarCodigo404() {
        NegocioException ex = new NegocioException(404, "Recurso não encontrado");

        Response response = mapper.toResponse(ex);

        assertEquals(404, response.getStatus());
        ErrorResponse body = (ErrorResponse) response.getEntity();
        assertEquals("Recurso não encontrado", body.getMensagem());
    }

    @Test
    @DisplayName("Deve usar o código informado na exception (400)")
    void deveUsarCodigo400() {
        NegocioException ex = new NegocioException(400, "Dados inválidos");

        Response response = mapper.toResponse(ex);

        assertEquals(400, response.getStatus());
        ErrorResponse body = (ErrorResponse) response.getEntity();
        assertEquals("Dados inválidos", body.getMensagem());
    }
}
