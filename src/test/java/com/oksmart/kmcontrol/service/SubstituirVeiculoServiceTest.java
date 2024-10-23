package com.oksmart.kmcontrol.service;

import com.oksmart.kmcontrol.dto.SubstituirVeiculoDTO;
import com.oksmart.kmcontrol.dto.ContratoDTO;
import com.oksmart.kmcontrol.exception.ServiceException;
import com.oksmart.kmcontrol.model.ContratoModel;
import com.oksmart.kmcontrol.repository.ContratoRepository;
import com.oksmart.kmcontrol.service.converter.ContratoConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class SubstituirVeiculoServiceTest {

    @InjectMocks
    private SubstituirVeiculoService substituirVeiculoService;

    @Mock
    private ContratoRepository contratoRepository;

    @Mock
    private ContratoConverter contratoConverter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSubstituirVeiculo_Success() {
        SubstituirVeiculoDTO dto = new SubstituirVeiculoDTO();
        dto.setNumeroContrato("1234");

        ContratoModel model = new ContratoModel();
        // Preencher o modelo conforme necessário

        when(contratoRepository.findByNumeroContrato(dto.getNumeroContrato())).thenReturn(Collections.singletonList(model));
        when(contratoConverter.convertToDTO(model)).thenReturn(new ContratoDTO());

        ContratoDTO result = substituirVeiculoService.substituirVeiculo(dto);
        assertNotNull(result);
        // Mais asserts conforme necessário
    }

    @Test
    public void testSubstituirVeiculo_ServiceException() {
        SubstituirVeiculoDTO dto = new SubstituirVeiculoDTO();
        dto.setNumeroContrato("1234");

        when(contratoRepository.findByNumeroContrato(dto.getNumeroContrato())).thenReturn(Collections.emptyList());

        assertThrows(ServiceException.class, () -> {
            substituirVeiculoService.substituirVeiculo(dto);
        });
    }
}
