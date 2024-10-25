package com.oksmart.kmcontrol.service;

import com.oksmart.kmcontrol.exception.ServiceException;
import com.oksmart.kmcontrol.model.ContratoModel;
import com.oksmart.kmcontrol.repository.ContratoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeletarContratoService {

    @Autowired
    ContratoRepository contratoRepository;

    public void deletarContrato(String numeroContrato) {
        List<ContratoModel> contratos = contratoRepository.findByNumeroContrato(numeroContrato);
        if (contratos.isEmpty()) {
            throw new ServiceException("Contrato não encontrado para o número fornecido.");
        }
        contratoRepository.deleteAll(contratos);
    }
}

