package com.oksmart.kmcontrol.service;

import com.oksmart.kmcontrol.dto.ContratoDTO;
import com.oksmart.kmcontrol.model.ContratoModel;
import com.oksmart.kmcontrol.repository.ContratoRepository;
import com.oksmart.kmcontrol.service.converter.ContratoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListarTodosService {

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private ContratoConverter contratoConverter;

    public List<ContratoDTO> listarTodos(int page, int size) {
        Page<ContratoModel> contratosPage = contratoRepository.findAll(PageRequest.of(page, size));
        return contratoRepository.findAll().stream()
                .map(contratoConverter::convertToDTO)
                .collect(Collectors.toList());
    }
}
