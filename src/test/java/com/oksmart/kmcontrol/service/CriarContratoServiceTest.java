package com.oksmart.kmcontrol.service;

import com.oksmart.kmcontrol.dto.ContratoCreateDTO;
import com.oksmart.kmcontrol.dto.ContratoDTO;
import com.oksmart.kmcontrol.exception.ServiceException;
import com.oksmart.kmcontrol.model.ContratoModel;
import com.oksmart.kmcontrol.repository.ContratoRepository;
import com.oksmart.kmcontrol.service.converter.ContratoConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CriarContratoServiceTest {

    private CriarContratoService criarContratoService;
    private ContratoRepository contratoRepository;
    private ContratoConverter contratoConverter;

    @BeforeEach
    void setUp() {
        contratoRepository = mock(ContratoRepository.class);
        contratoConverter = mock(ContratoConverter.class);
        criarContratoService = new CriarContratoService();
        criarContratoService.contratoRepository = contratoRepository;
        criarContratoService.contratoConverter = contratoConverter;
    }

    @Test
    void testCriarContrato_Success() {
        ContratoCreateDTO dto = new ContratoCreateDTO();
        dto.setPlaca("ABC1234");
        dto.setNumeroContrato("123456");
        dto.setKmAtual(10000);
        dto.setKmInicial(5000);
        dto.setDataRegistro(LocalDate.now());

        ContratoModel contratoModel = new ContratoModel();
        when(contratoRepository.findAll()).thenReturn(Collections.emptyList());
        when(contratoRepository.save(any())).thenReturn(contratoModel);

        ContratoDTO contratoDTO = new ContratoDTO();
        when(contratoConverter.convertToDTO(any())).thenReturn(contratoDTO);

        ContratoDTO result = criarContratoService.criarContrato(dto);

        assertNotNull(result);
        verify(contratoRepository).save(any(ContratoModel.class));
    }

    @Test
    void testCriarContrato_ContratoExists() {
        ContratoCreateDTO dto = new ContratoCreateDTO();
        dto.setPlaca("ABC1234");
        dto.setNumeroContrato("123456");

        ContratoModel contratoModel = new ContratoModel();
        contratoModel.setPlaca("ABC1234");
        contratoModel.setNumeroContrato("123456");

        when(contratoRepository.findAll()).thenReturn(Collections.singletonList(contratoModel));

        Exception exception = assertThrows(ServiceException.class, () -> criarContratoService.criarContrato(dto));
        assertEquals("Já existe um contrato com a mesma placa ou número de contrato.", exception.getMessage());
    }
}
