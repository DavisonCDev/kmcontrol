package com.oksmart.kmcontrol.service;

import com.oksmart.kmcontrol.dto.ContratoDTO;
import com.oksmart.kmcontrol.dto.SubstituirVeiculoDTO;
import com.oksmart.kmcontrol.exception.ServiceException;
import com.oksmart.kmcontrol.model.ContratoModel;
import com.oksmart.kmcontrol.repository.ContratoRepository;
import com.oksmart.kmcontrol.service.converter.ContratoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SubstituirVeiculoService {

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private ContratoConverter contratoConverter;

    public ContratoDTO substituirVeiculo(SubstituirVeiculoDTO substituirVeiculoDTO) {
        // Busca o último contrato com o número do contrato fornecido
        List<ContratoModel> contratos =
                contratoRepository.findByNumeroContrato(substituirVeiculoDTO.getNumeroContrato());

        if (contratos.isEmpty()) {
            throw new ServiceException("Contrato não encontrado para o número fornecido.");
        }

        ContratoModel ultimoContrato = contratos.get(0);
        ContratoModel novoContrato = new ContratoModel();


        // Copia todos os dados do último contrato, exceto os que precisam ser alterados0
        novoContrato.setCondutorPrincipal(ultimoContrato.getCondutorPrincipal());
        novoContrato.setCondutorResponsavel(ultimoContrato.getCondutorResponsavel());
        novoContrato.setDataVigencia(ultimoContrato.getDataVigencia());
        novoContrato.setDataRegistro(ultimoContrato.getDataRegistro());
        novoContrato.setDiarias(ultimoContrato.getDiarias());
        novoContrato.setFranquiaKm(ultimoContrato.getFranquiaKm());
        novoContrato.setLocadora(ultimoContrato.getLocadora());
        novoContrato.setOsCliente(ultimoContrato.getOsCliente());
        novoContrato.setQtMesesVeic(ultimoContrato.getQtMesesVeic());
        novoContrato.setValorAluguel(ultimoContrato.getValorAluguel());
        novoContrato.setDataAtual(LocalDate.now());
        novoContrato.setKmMediaMensal(ultimoContrato.getKmMediaMensal());

        // Define os novos dados
        novoContrato.setDataSubstituicao(substituirVeiculoDTO.getDataSubstituicao());
        novoContrato.setKmInicial(substituirVeiculoDTO.getKmInicial());
        novoContrato.setKmAtual(substituirVeiculoDTO.getKmInicial());
        novoContrato.setKmIdeal(substituirVeiculoDTO.getKmInicial());
        novoContrato.setMarca(substituirVeiculoDTO.getMarca());
        novoContrato.setModelo(substituirVeiculoDTO.getModelo());
        novoContrato.setPlaca(substituirVeiculoDTO.getPlaca());
        novoContrato.setNumeroContrato(substituirVeiculoDTO.getNumeroContrato());
        // Define a data atual
        novoContrato.setDataAtual(LocalDate.now());


        // Salva o novo contrato no banco de dados
        ContratoModel savedContrato = contratoRepository.save(novoContrato);
        return contratoConverter.convertToDTO(savedContrato);
    }
}
