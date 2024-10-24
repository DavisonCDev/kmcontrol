package com.oksmart.kmcontrol.service;

import com.oksmart.kmcontrol.dto.ContratoDTO;
import com.oksmart.kmcontrol.model.ContratoModel;
import com.oksmart.kmcontrol.repository.ContratoRepository;
import com.oksmart.kmcontrol.service.converter.ContratoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ListarTodosService {

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private ContratoConverter contratoConverter;

    public Page<ContratoDTO> listarTodos(int page, int size) {
        // Obtém a página de ContratoModel
        Page<ContratoModel> contratosPage = contratoRepository.findAll(PageRequest.of(page, size));

        // Converte a página de ContratoModel para uma página de ContratoDTO
        return contratosPage.map(contratoConverter::convertToDTO);
    }
}
