package com.oksmart.kmcontrol.exception;

import com.oksmart.kmcontrol.dto.ApiResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    public void testHandleServiceException() {
        ServiceException exception = new ServiceException("Erro específico");

        ResponseEntity<ApiResponseDTO<Void>> response = globalExceptionHandler.handleServiceException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erro específico", response.getBody().getMessage());
    }

    @Test
    public void testHandleGenericException() {
        Exception exception = new Exception("Erro genérico");

        ResponseEntity<ApiResponseDTO<Void>> response = globalExceptionHandler.handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Erro: Erro genérico", response.getBody().getMessage());
    }
}
