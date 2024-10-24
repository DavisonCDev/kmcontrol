package com.oksmart.kmcontrol.service.converter;

import com.oksmart.kmcontrol.dto.ContratoCreateDTO;
import com.oksmart.kmcontrol.dto.ContratoDTO;
import com.oksmart.kmcontrol.model.ContratoModel;
import org.springframework.stereotype.Component;

@Component
public class ContratoConverter {

    // Método para conversão de ContratoModel para ContratoDTO
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

    // Método para conversão de ContratoDTO para ContratoModel
    public ContratoModel convertToModel(ContratoDTO contratoDTO) {
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

    // Método para conversão de ContratoCreateDTO para ContratoModel
    public ContratoModel convertToModel(ContratoCreateDTO contratoCreateDTO) {
        return new ContratoModel(
                null,  // ID gerado pelo banco
                contratoCreateDTO.getCondutorPrincipal(),
                contratoCreateDTO.getCondutorResponsavel(),
                null,  // Data atual será setada posteriormente
                contratoCreateDTO.getDataRegistro(),
                contratoCreateDTO.getDataVigencia(),
                null,  // Data de substituição
                contratoCreateDTO.getDiarias(),
                contratoCreateDTO.getFranquiaKm(),
                contratoCreateDTO.getKmAtual(),
                contratoCreateDTO.getKmInicial(),
                contratoCreateDTO.getLocadora(),
                contratoCreateDTO.getMarca(),
                contratoCreateDTO.getModelo(),
                contratoCreateDTO.getNumeroContrato(),
                contratoCreateDTO.getOsCliente(),
                contratoCreateDTO.getPlaca(),
                contratoCreateDTO.getValorAluguel(),
                false, // Fazer revisão inicial como false
                false, // Km excedido inicial como false
                0,     // Km ideal inicial
                0,     // Km semana inicial
                0,     // Km média mensal inicial
                0,     // Qt meses veículo inicial
                0,     // Qt meses cont inicial
                0,     // Saldo km inicial
                0,     // Acumulado mês inicial
                null,  // Entrega prop data
                0,     // Contador revisão inicial
                null   // Observações iniciais
        );
    }
}
