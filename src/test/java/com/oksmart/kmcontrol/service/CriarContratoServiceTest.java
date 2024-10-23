package com.oksmart.kmcontrol.service;

import com.oksmart.kmcontrol.dto.ContratoCreateDTO;
import com.oksmart.kmcontrol.dto.ContratoDTO;
import com.oksmart.kmcontrol.exception.ServiceException;
import com.oksmart.kmcontrol.model.ContratoModel;
import com.oksmart.kmcontrol.repository.ContratoRepository;
import com.oksmart.kmcontrol.service.converter.ContratoConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CriarContratoServiceTest {

    @InjectMocks
    private CriarContratoService criarContratoService;

    @Mock
    private ContratoRepository contratoRepository;

    @Mock
    private ContratoConverter contratoConverter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCriarContrato_Success() {
        ContratoCreateDTO dto = new ContratoCreateDTO();
        // Preencher o DTO conforme necessário

        ContratoModel model = new ContratoModel();
        // Preencher o modelo conforme necessário

        when(contratoConverter.convertToModel(dto)).thenReturn(model);
        when(contratoRepository.save(model)).thenReturn(model);
        when(contratoConverter.convertToDTO(model)).thenReturn(new ContratoDTO());

        ContratoDTO result = criarContratoService.criarContrato(dto);
        assertNotNull(result);
        // Mais asserts conforme necessário
    }

    @Test
    public void testCriarContrato_ServiceException() {
        ContratoCreateDTO dto = new ContratoCreateDTO();
        // Preencher o DTO conforme necessário

        when(contratoRepository.save(any())).thenThrow(new RuntimeException("Erro ao salvar contrato"));

        assertThrows(ServiceException.class, () -> {
            criarContratoService.criarContrato(dto);
        });
    }
}
