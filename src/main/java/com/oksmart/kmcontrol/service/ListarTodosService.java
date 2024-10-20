package com.oksmart.kmcontrol.service;

import com.oksmart.kmcontrol.dto.ContratoDTO;
import com.oksmart.kmcontrol.model.ContratoModel;
import com.oksmart.kmcontrol.repository.ContratoRepository;
import com.oksmart.kmcontrol.service.converter.ContratoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListarTodosService {

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private ContratoConverter contratoConverter;

    public List<ContratoDTO> listarTodos() {
        return contratoRepository.findAll().stream()
                .map(contratoConverter::convertToDTO)
                .collect(Collectors.toList());
    }
}
