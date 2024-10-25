package com.oksmart.kmcontrol.service;

import com.oksmart.kmcontrol.exception.ServiceException;
import com.oksmart.kmcontrol.model.ContratoModel;
import com.oksmart.kmcontrol.repository.ContratoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeletarContratoServiceTest {

    private DeletarContratoService deletarContratoService;
    private ContratoRepository contratoRepository;

    @BeforeEach
    void setUp() {
        contratoRepository = mock(ContratoRepository.class);
        deletarContratoService = new DeletarContratoService();
        deletarContratoService.contratoRepository = contratoRepository;
    }

    @Test
    void testDeletarContrato_Success() {
        String numeroContrato = "123456";
        when(contratoRepository.findByNumeroContrato(numeroContrato)).thenReturn(Collections.singletonList(new ContratoModel()));

        deletarContratoService.deletarContrato(numeroContrato);

        verify(contratoRepository).deleteAll(any());
    }

    @Test
    void testDeletarContrato_ContratoNotFound() {
        String numeroContrato = "XYZ9999";
        when(contratoRepository.findByNumeroContrato(numeroContrato)).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(ServiceException.class, () -> deletarContratoService.deletarContrato(numeroContrato));
        assertEquals("Contrato não encontrado para o número fornecido.", exception.getMessage());
    }
}
