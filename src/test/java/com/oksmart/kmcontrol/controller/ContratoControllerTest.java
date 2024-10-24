package com.oksmart.kmcontrol.controller;

import com.oksmart.kmcontrol.dto.ApiResponseDTO;
import com.oksmart.kmcontrol.dto.ContratoCreateDTO;
import com.oksmart.kmcontrol.dto.ContratoDTO;
import com.oksmart.kmcontrol.exception.ServiceException;
import com.oksmart.kmcontrol.service.CriarContratoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ContratoControllerTest {

    @InjectMocks
    private ContratoController contratoController;

    @Mock
    private CriarContratoService criarContratoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCriarContrato_Success() {
        ContratoCreateDTO dto = new ContratoCreateDTO();
        // Preencher o DTO conforme necessário

        ContratoDTO expectedResponse = new ContratoDTO();
        // Preencher a resposta esperada conforme necessário

        when(criarContratoService.criarContrato(dto)).thenReturn(expectedResponse);

        ResponseEntity<ApiResponseDTO<ContratoDTO>> response = contratoController.criarContrato(dto);

        assertEquals(200, response.getStatusCodeValue());
        // Mais asserts conforme necessário
    }

    @Test
    public void testCriarContrato_ServiceException() {
        ContratoCreateDTO dto = new ContratoCreateDTO();
        // Preencher o DTO conforme necessário

        when(criarContratoService.criarContrato(dto)).thenThrow(new ServiceException("Erro ao criar contrato"));

        ResponseEntity<ApiResponseDTO<ContratoDTO>> response = contratoController.criarContrato(dto);

        assertEquals(400, response.getStatusCodeValue());
        // Mais asserts conforme necessário
    }
}
