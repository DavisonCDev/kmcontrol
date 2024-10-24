package com.oksmart.kmcontrol.exception;

import com.oksmart.kmcontrol.dto.ApiResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleServiceException(ServiceException e) {
        ApiResponseDTO<Void> response = new ApiResponseDTO<>("error",
                e.getErrorMessage(), null, null);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleGenericException(Exception e) {
        logger.error("Erro não tratado: ", e); // Log da exceção
        ApiResponseDTO<Void> response = new ApiResponseDTO<>("error",
                "Erro: " + e.getMessage(), null, null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
