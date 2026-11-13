// Traduz BusinessException (domínio) → resposta HTTP 400 (camada web)
package com.leozito.exception;

import com.leozito.domain.BusinessException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

// @Provider: o Quarkus registra a classe automaticamente no boot
@Provider
public class BusinessExceptionMapper implements ExceptionMapper<BusinessException> {

    @Override
    public Response toResponse(BusinessException exception) {
        // 400 = cliente enviou algo inválido (regra de negócio), não 500
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(exception.getMessage()) // corpo = mensagem da regra
                .build();
    }
}