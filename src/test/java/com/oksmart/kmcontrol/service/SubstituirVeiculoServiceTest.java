package com.oksmart.kmcontrol.service;

import com.oksmart.kmcontrol.dto.SubstituirVeiculoDTO;
import com.oksmart.kmcontrol.dto.ContratoDTO;
import com.oksmart.kmcontrol.exception.ServiceException;
import com.oksmart.kmcontrol.model.ContratoModel;
import com.oksmart.kmcontrol.repository.ContratoRepository;
import com.oksmart.kmcontrol.service.converter.ContratoConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubstituirVeiculoServiceTest {

    private SubstituirVeiculoService substituirVeiculoService;
    private ContratoRepository contratoRepository;
    private ContratoConverter contratoConverter;

    @BeforeEach
    void setUp() {
        contratoRepository = mock(ContratoRepository.class);
        contratoConverter = mock(ContratoConverter.class);
        substituirVeiculoService = new SubstituirVeiculoService();
        substituirVeiculoService.contratoRepository = contratoRepository;
        substituirVeiculoService.contratoConverter = contratoConverter;
    }

    @Test
    void testSubstituirVeiculo_Success() {
        SubstituirVeiculoDTO dto = new SubstituirVeiculoDTO();
        dto.setNumeroContrato("123456");
        dto.setKmInicial(10000);
        dto.setMarca("Marca");
        dto.setModelo("Modelo");
        dto.setPlaca("ABC1234");
        dto.setDataSubstituicao(LocalDate.now());

        ContratoModel ultimoContrato = new ContratoModel();
        ultimoContrato.setCondutorPrincipal("Condutor Principal");
        ultimoContrato.setCondutorResponsavel("Condutor Responsável");
        // Adicione outros dados conforme necessário

        when(contratoRepository.findByNumeroContrato(dto.getNumeroContrato())).thenReturn(Collections.singletonList(ultimoContrato));
        when(contratoRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        ContratoDTO contratoDTO = new ContratoDTO();
        when(contratoConverter.convertToDTO(any())).thenReturn(contratoDTO);

        ContratoDTO result = substituirVeiculoService.substituirVeiculo(dto);

        assertNotNull(result);
        verify(contratoRepository).save(any(ContratoModel.class));
    }

    @Test
    void testSubstituirVeiculo_ContratoNotFound() {
        SubstituirVeiculoDTO dto = new SubstituirVeiculoDTO();
        dto.setNumeroContrato("XYZ9999");

        when(contratoRepository.findByNumeroContrato(dto.getNumeroContrato())).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(ServiceException.class, () -> substituirVeiculoService.substituirVeiculo(dto));
        assertEquals("Contrato não encontrado para o número fornecido.", exception.getMessage());
    }
}
