package com.oksmart.kmcontrol.service.converter;

import com.oksmart.kmcontrol.dto.ContratoCreateDTO;
import com.oksmart.kmcontrol.dto.ContratoDTO;
import com.oksmart.kmcontrol.model.ContratoModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContratoConverterTest {

    private final ContratoConverter contratoConverter = new ContratoConverter();

    @Test
    void testConvertToDTO() {
        ContratoModel model = new ContratoModel();
        model.setId(1L);
        model.setCondutorPrincipal("Condutor Principal");

        ContratoDTO dto = contratoConverter.convertToDTO(model);

        assertNotNull(dto);
        assertEquals(model.getId(), dto.getId());
        assertEquals(model.getCondutorPrincipal(), dto.getCondutorPrincipal());
    }

    @Test
    void testConvertToModel() {
        ContratoDTO dto = new ContratoDTO();
        dto.setId(1L);
        dto.setCondutorPrincipal("Condutor Principal");

        ContratoModel model = contratoConverter.convertToModel(dto);

        assertNotNull(model);
        assertEquals(dto.getId(), model.getId());
        assertEquals(dto.getCondutorPrincipal(), model.getCondutorPrincipal());
    }

    @Test
    void testConvertToModelFromCreateDTO() {
        ContratoCreateDTO createDTO = new ContratoCreateDTO();
        createDTO.setCondutorPrincipal("Condutor Principal");
        createDTO.setLocadora("Locadora");

        ContratoModel model = contratoConverter.convertToModel(createDTO);

        assertNotNull(model);
        assertEquals(createDTO.getCondutorPrincipal(), model.getCondutorPrincipal());
        assertEquals(createDTO.getLocadora(), model.getLocadora());
    }
}
