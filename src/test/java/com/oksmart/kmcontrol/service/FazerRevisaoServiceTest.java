package com.oksmart.kmcontrol.service;

import com.oksmart.kmcontrol.dto.FazerRevisaoDTO;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class FazerRevisaoServiceTest {

    @InjectMocks
    private FazerRevisaoService fazerRevisaoService;

    @Mock
    private ContratoRepository contratoRepository;

    @Mock
    private ContratoConverter contratoConverter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFazerRevisao_Success() {
        FazerRevisaoDTO dto = new FazerRevisaoDTO();
        dto.setPlaca("ABC-1234");

        ContratoModel model = new ContratoModel();
        // Preencher o modelo conforme necessário

        when(contratoRepository.findByPlaca(dto.getPlaca())).thenReturn(Collections.singletonList(model));
        when(contratoConverter.convertToDTO(model)).thenReturn(new ContratoDTO());

        ContratoDTO result = fazerRevisaoService.fazerRevisao(dto);
        assertNotNull(result);
        // Mais asserts conforme necessário
    }

    @Test
    public void testFazerRevisao_ServiceException() {
        FazerRevisaoDTO dto = new FazerRevisaoDTO();
        dto.setPlaca("ABC-1234");

        when(contratoRepository.findByPlaca(dto.getPlaca())).thenReturn(Collections.emptyList());

        assertThrows(ServiceException.class, () -> {
            fazerRevisaoService.fazerRevisao(dto);
        });
    }
}
