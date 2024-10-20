package com.oksmart.kmcontrol.service.converter;

import com.oksmart.kmcontrol.dto.ContratoDTO;
import com.oksmart.kmcontrol.model.ContratoModel;
import org.springframework.stereotype.Component;

@Component
public class ContratoConverter {
    // Método para conversões DTO
    public ContratoDTO convertToDTO(ContratoModel contrato) {
        return new ContratoDTO(
                contrato.getId(),
                contrato.getCondutorPrincipal(),
                contrato.getCondutorResponsavel(),
                contrato.getDataAtual(),
                contrato.getDataRegistro(),
                contrato.getDataVigencia(),
                contrato.getDataSubstituicao(),
                contrato.getDiarias(),
                contrato.getFranquiaKm(),
                contrato.getKmAtual(),
                contrato.getKmInicial(),
                contrato.getLocadora(),
                contrato.getMarca(),
                contrato.getModelo(),
                contrato.getNumeroContrato(),
                contrato.getOsCliente(),
                contrato.getPlaca(),
                contrato.getValorAluguel(),
                contrato.isFazerRevisao(),
                contrato.isKmExcedido(),
                contrato.getKmIdeal(),
                contrato.getKmSemana(),
                contrato.getKmMediaMensal(),
                contrato.getQtMesesVeic(),
                contrato.getQtMesesCont(),
                contrato.getSaldoKm(),
                contrato.getAcumuladoMes(),
                contrato.getEntregaPropData(),
                contrato.getContadorRevisao(),
                contrato.getObservacoes()
        );
    }

    // Métodos para conversão para Model
    private ContratoModel convertToModel(ContratoDTO contratoDTO) {
        return new ContratoModel(
                contratoDTO.getId(),
                contratoDTO.getCondutorPrincipal(),
                contratoDTO.getCondutorResponsavel(),
                contratoDTO.getDataAtual(),
                contratoDTO.getDataRegistro(),
                contratoDTO.getDataVigencia(),
                contratoDTO.getDataSubstituicao(),
                contratoDTO.getDiarias(),
                contratoDTO.getFranquiaKm(),
                contratoDTO.getKmAtual(),
                contratoDTO.getKmInicial(),
                contratoDTO.getLocadora(),
                contratoDTO.getMarca(),
                contratoDTO.getModelo(),
                contratoDTO.getNumeroContrato(),
                contratoDTO.getOsCliente(),
                contratoDTO.getPlaca(),
                contratoDTO.getValorAluguel(),
                contratoDTO.isFazerRevisao(),
                contratoDTO.isKmExcedido(),
                contratoDTO.getKmIdeal(),
                contratoDTO.getKmSemana(),
                contratoDTO.getKmMediaMensal(),
                contratoDTO.getQtMesesVeic(),
                contratoDTO.getQtMesesCont(),
                contratoDTO.getSaldoKm(),
                contratoDTO.getAcumuladoMes(),
                contratoDTO.getEntregaPropData(),
                contratoDTO.getContadorRevisao(),
                contratoDTO.getObservacoes()
        );
    }
}
