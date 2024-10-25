package com.oksmart.kmcontrol.service;

import com.oksmart.kmcontrol.dto.ContratoDTO;
import com.oksmart.kmcontrol.model.ContratoModel;
import com.oksmart.kmcontrol.repository.ContratoRepository;
import com.oksmart.kmcontrol.service.converter.ContratoConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListarContratosServiceTest {

    private ListarContratosService listarContratosService;
    private ContratoRepository contratoRepository;
    private ContratoConverter contratoConverter;

    @BeforeEach
    void setUp() {
        contratoRepository = mock(ContratoRepository.class);
        contratoConverter = mock(ContratoConverter.class);
        listarContratosService = new ListarContratosService();
        listarContratosService.contratoRepository = contratoRepository;
        listarContratosService.contratoConverter = contratoConverter;
    }

    @Test
    void testListarUltimosContratos_Success() {
        ContratoModel contrato = new ContratoModel();
        List<ContratoModel> contratos = Arrays.asList(contrato);

        when(contratoRepository.findUltimosContratos()).thenReturn(contratos);
        when(contratoConverter.convertToDTO(contrato)).thenReturn(new ContratoDTO());

        List<ContratoDTO> result = listarContratosService.listarUltimosContratos();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(contratoRepository).findUltimosContratos();
    }
}
