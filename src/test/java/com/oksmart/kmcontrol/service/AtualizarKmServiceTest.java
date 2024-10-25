package com.oksmart.kmcontrol.service;

import com.oksmart.kmcontrol.dto.AtualizarKmDTO;
import com.oksmart.kmcontrol.dto.ContratoDTO;
import com.oksmart.kmcontrol.exception.ServiceException;
import com.oksmart.kmcontrol.model.ContratoModel;
import com.oksmart.kmcontrol.repository.ContratoRepository;
import com.oksmart.kmcontrol.service.converter.ContratoConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AtualizarKmServiceTest {

    private AtualizarKmService atualizarKmService;
    private ContratoRepository contratoRepository;
    private ContratoConverter contratoConverter;

    @BeforeEach
    void setUp() {
        contratoRepository = mock(ContratoRepository.class);
        contratoConverter = mock(ContratoConverter.class);
        atualizarKmService = new AtualizarKmService();
        atualizarKmService.contratoRepository = contratoRepository;
        atualizarKmService.contratoConverter = contratoConverter;
    }

    @Test
    void testAtualizarKm_Success() {
        AtualizarKmDTO dto = new AtualizarKmDTO();
        dto.setPlaca("ABC1234");
        dto.setKmAtual(15000); // O novo valor de quilometragem

        ContratoModel contrato = new ContratoModel();
        contrato.setPlaca("ABC1234");
        contrato.setKmAtual(12000); // Valor anterior menor que o novo
        contrato.setDataRegistro(LocalDate.now().minusMonths(1));
        contrato.setKmInicial(10000);
        contrato.setDiarias(30);
        contrato.setFranquiaKm(1000);
        contrato.setValorAluguel(2000);

        // Mockando o repositório para retornar o contrato existente
        when(contratoRepository.findByPlaca(dto.getPlaca())).thenReturn(Collections.singletonList(contrato));
        when(contratoRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        ContratoDTO contratoDTO = new ContratoDTO();
        when(contratoConverter.convertToDTO(any())).thenReturn(contratoDTO);
        contratoDTO.setKmAtual(15000); // Defina o kmAtual que deve ser retornado

        // Chamada ao método que está sendo testado
        ContratoDTO result = atualizarKmService.atualizarKm(dto);

        assertNotNull(result);
        assertEquals(15000, result.getKmAtual()); // Verificando se o kmAtual foi atualizado corretamente
        verify(contratoRepository).save(any(ContratoModel.class)); // Verificando se o save foi chamado
    }



    @Test
    void testAtualizarKm_ContratoNotFound() {
        AtualizarKmDTO dto = new AtualizarKmDTO();
        dto.setPlaca("XYZ9999");

        when(contratoRepository.findByPlaca(dto.getPlaca())).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(ServiceException.class, () -> atualizarKmService.atualizarKm(dto));
        assertEquals("Contrato não encontrado para a placa fornecida.", exception.getMessage());
    }

    @Test
    void testAtualizarKm_ExcedeKmIdeal() {
        AtualizarKmDTO dto = new AtualizarKmDTO();
        dto.setPlaca("ABC1234");
        dto.setKmAtual(12000);  // Km igual ao anterior

        ContratoModel contrato = new ContratoModel();
        contrato.setPlaca("ABC1234");
        contrato.setKmAtual(12000); // Valor anterior igual ao novo
        contrato.setKmInicial(10000);
        contrato.setFranquiaKm(1000);
        contrato.setDataRegistro(LocalDate.now().minusMonths(3));
        contrato.setValorAluguel(2000);

        when(contratoRepository.findByPlaca(dto.getPlaca())).thenReturn(Collections.singletonList(contrato));

        Exception exception = assertThrows(ServiceException.class, () -> atualizarKmService.atualizarKm(dto));
        assertEquals("O Km Atual deve ser maior que: 12000 KM.", exception.getMessage());
    }

}
