package br.gov.caixa.domain.exception;

import br.gov.caixa.adapters.inbound.rest.ErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NegocioExceptionMapper implements ExceptionMapper<NegocioException> {
    @Override
    public Response toResponse(NegocioException exception) {
        ErrorResponse error = new ErrorResponse(
                exception.getMessage()
        );
        return Response.status(exception.getCodigo()).entity(error).build();
    }
}
