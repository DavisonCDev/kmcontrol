package com.oksmart.kmcontrol.service;

import com.oksmart.kmcontrol.dto.ContratoDTO;
import com.oksmart.kmcontrol.dto.FazerRevisaoDTO;
import com.oksmart.kmcontrol.exception.ServiceException;
import com.oksmart.kmcontrol.model.ContratoModel;
import com.oksmart.kmcontrol.repository.ContratoRepository;
import com.oksmart.kmcontrol.service.converter.ContratoConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FazerRevisaoServiceTest {

    private FazerRevisaoService fazerRevisaoService;
    private ContratoRepository contratoRepository;
    private ContratoConverter contratoConverter;

    @BeforeEach
    void setUp() {
        contratoRepository = mock(ContratoRepository.class);
        contratoConverter = mock(ContratoConverter.class);
        fazerRevisaoService = new FazerRevisaoService();
        fazerRevisaoService.contratoRepository = contratoRepository;
        fazerRevisaoService.contratoConverter = contratoConverter;
    }

    @Test
    void testFazerRevisao_Success() {
        FazerRevisaoDTO dto = new FazerRevisaoDTO();
        dto.setPlaca("ABC1234");

        ContratoModel contrato = new ContratoModel();
        contrato.setPlaca("ABC1234");
        contrato.setKmAtual(15000);
        contrato.setDiarias(5);
        contrato.setDataRegistro(LocalDate.now().minusMonths(1));

        when(contratoRepository.findByPlaca(dto.getPlaca())).thenReturn(Collections.singletonList(contrato));
        when(contratoRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // Criar um ContratoDTO para retornar do mock
        ContratoDTO contratoDTO = new ContratoDTO();
        contratoDTO.setPlaca("ABC1234"); // Preencha com dados necessários
        contratoDTO.setKmAtual(15000);
        contratoDTO.setDiarias(5);
        contratoDTO.setDataRegistro(LocalDate.now().minusMonths(1));

        // Altere o mock para retornar ContratoDTO
        when(contratoConverter.convertToDTO(any())).thenReturn(contratoDTO);

        ContratoDTO result = fazerRevisaoService.fazerRevisao(dto);

        assertNotNull(result);
        verify(contratoRepository).save(any(ContratoModel.class));
    }

    @Test
    void testFazerRevisao_ContratoNotFound() {
        FazerRevisaoDTO dto = new FazerRevisaoDTO();
        dto.setPlaca("XYZ9999");

        when(contratoRepository.findByPlaca(dto.getPlaca())).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(ServiceException.class, () -> fazerRevisaoService.fazerRevisao(dto));
        assertEquals("Contrato não encontrado para a placa fornecida.", exception.getMessage());
    }
}
