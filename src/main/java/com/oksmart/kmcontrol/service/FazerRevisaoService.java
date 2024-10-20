package com.oksmart.kmcontrol.service;

import com.oksmart.kmcontrol.dto.ContratoDTO;
import com.oksmart.kmcontrol.dto.FazerRevisaoDTO;
import com.oksmart.kmcontrol.model.ContratoModel;
import com.oksmart.kmcontrol.repository.ContratoRepository;
import com.oksmart.kmcontrol.service.converter.ContratoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FazerRevisaoService {

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private ContratoConverter contratoConverter;

    public ContratoDTO fazerRevisao(FazerRevisaoDTO fazerRevisaoDTO) {
        List<ContratoModel> contratos = contratoRepository.findByPlaca(fazerRevisaoDTO.getPlaca());

        if (contratos.isEmpty()) {
            throw new IllegalArgumentException("Contrato não encontrado para a placa fornecida.");
        }

        ContratoModel ultimoContrato = contratos.get(0);

        ContratoModel novoContrato = new ContratoModel();
        novoContrato.setPlaca(ultimoContrato.getPlaca());
        novoContrato.setKmAtual(0); // ContadorRevisao zerado
        novoContrato.setFazerRevisao(false); // Fazer revisão falso
        novoContrato.setDataAtual(LocalDate.now()); // Data atual

        // Copia todos os outros dados do último contrato
        novoContrato.setCondutorPrincipal(ultimoContrato.getCondutorPrincipal());
        novoContrato.setCondutorResponsavel(ultimoContrato.getCondutorResponsavel());
        novoContrato.setDiarias(ultimoContrato.getDiarias());
        novoContrato.setDataRegistro(ultimoContrato.getDataRegistro());
        novoContrato.setDataVigencia(ultimoContrato.getDataVigencia());
        novoContrato.setFranquiaKm(ultimoContrato.getFranquiaKm());
        novoContrato.setKmInicial(ultimoContrato.getKmInicial());
        novoContrato.setLocadora(ultimoContrato.getLocadora());
        novoContrato.setMarca(ultimoContrato.getMarca());
        novoContrato.setModelo(ultimoContrato.getModelo());
        novoContrato.setNumeroContrato(ultimoContrato.getNumeroContrato());
        novoContrato.setOsCliente(ultimoContrato.getOsCliente());
        novoContrato.setValorAluguel(ultimoContrato.getValorAluguel());
        novoContrato.setQtMesesVeic(ultimoContrato.getQtMesesVeic());
        novoContrato.setKmMediaMensal(ultimoContrato.getKmMediaMensal());
        novoContrato.setKmIdeal(ultimoContrato.getKmIdeal());
        novoContrato.setKmAtual(ultimoContrato.getKmAtual());
        novoContrato.setKmExcedido(ultimoContrato.isKmExcedido());
        novoContrato.setAcumuladoMes(ultimoContrato.getAcumuladoMes());
        novoContrato.setSaldoKm(ultimoContrato.getSaldoKm());
        novoContrato.setContadorRevisao(0); // Contador de revisão zerado

        // Define observações
        StringBuilder observacoes = new StringBuilder();

        if (novoContrato.isFazerRevisao()) {
            observacoes.append("Necessário marcar a revisão");
        }

        if (novoContrato.isKmExcedido()) {
            if (observacoes.length() > 0) {
                observacoes.append(" | ");
            }
            observacoes.append("Km Excedido: ").append(novoContrato.getAcumuladoMes());
        } else {
            if (observacoes.length() > 0) {
                observacoes.append(" | ");
            }
            observacoes.append("Km Livre: ").append(novoContrato.getAcumuladoMes());
        }

        novoContrato.setObservacoes(observacoes.toString());

        ContratoModel savedContrato = contratoRepository.save(novoContrato);
        return contratoConverter.convertToDTO(savedContrato);
    }
}
