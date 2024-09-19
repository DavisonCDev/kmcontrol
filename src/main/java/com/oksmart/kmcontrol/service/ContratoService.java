package com.oksmart.kmcontrol.service;

import com.oksmart.kmcontrol.dto.ContratoCreateDTO;
import com.oksmart.kmcontrol.dto.ContratoDTO;
import com.oksmart.kmcontrol.model.ContratoModel;
import com.oksmart.kmcontrol.repository.ContratoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContratoService {

    @Autowired
    private ContratoRepository contratoRepository;


    //Método para listar todos os contratos
    public List<ContratoDTO> listarTodos() {
        return contratoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    //Método para criar um contrato
    public ContratoDTO criarContrato(ContratoCreateDTO contratoCreateDTO) {
        ContratoModel contrato = new ContratoModel();
        contrato.setCondutorPrincipal(contratoCreateDTO.getCondutorPrincipal());
        contrato.setCondutorResponsavel(contratoCreateDTO.getCondutorResponsavel());

        // Define dataAtual como a data atual
        LocalDate dataAtual = LocalDate.now();
        contrato.setDataAtual(dataAtual);

        // Define dataRegistro a partir do DTO
        LocalDate dataRegistro = contratoCreateDTO.getDataRegistro();
        contrato.setDataRegistro(dataRegistro);

        // Calcula a quantidade de meses entre dataRegistro e dataAtual
        long quantiaMeses = ChronoUnit.MONTHS.between(dataRegistro, dataAtual);
        contrato.setQuantiaMeses((int) quantiaMeses); // Armazena o valor no campo quantiaMeses

        // Calcula kmMediaMensal
        int mesesParaCalculo = quantiaMeses == 0 ? 1 : (int) quantiaMeses; // Considera 1 se quantiaMeses for 0
        double kmMediaMensal = (double) contratoCreateDTO.getKmAtual() / mesesParaCalculo; // Calcula kmMediaMensal
        contrato.setKmMediaMensal((long) kmMediaMensal); // Armazena no campo kmMediaMensal

        // Calcula contadorRevisao
        int contadorRevisao = contratoCreateDTO.getKmAtual() - contratoCreateDTO.getKmInicial();
        contrato.setContadorRevisao(contadorRevisao); // Armazena no campo contadorRevisao

        // Verifica se deve fazer revisão
        contrato.setFazerRevisao(contadorRevisao > 10000); // Se contadorRevisao > 10000, fazerRevisao é true

        // Calcula kmIdeal
        int mesesParaKmIdeal = quantiaMeses == 0 ? 1 : (int) quantiaMeses; // Considera 1 se quantiaMeses for 0
        int kmIdeal = contratoCreateDTO.getFranquiaKm() * mesesParaKmIdeal; // Multiplica franquiaKm pela quantidade de meses
        contrato.setKmIdeal(kmIdeal); // Armazena no campo kmIdeal

        // Calcula acumuladoMes
        long acumuladoMes = kmIdeal - contratoCreateDTO.getKmAtual(); // Subtrai kmAtual de kmIdeal
        contrato.setAcumuladoMes(acumuladoMes); // Armazena no campo acumuladoMes

        // Define kmExcedido com base no acumuladoMes
        contrato.setKmExcedido(acumuladoMes < 0); // Se acumuladoMes for menor que 0, kmExcedido é true, caso contrário, false

        // Calcula saldoKm
        double saldoKm = (contratoCreateDTO.getValorAluguel() / contratoCreateDTO.getFranquiaKm()) * acumuladoMes; // Saldo calculado
        contrato.setSaldoKm(saldoKm); // Armazena no campo saldoKm

        contrato.setDiarias(contratoCreateDTO.getDiarias());
        contrato.setFranquiaKm(contratoCreateDTO.getFranquiaKm());
        contrato.setKmAtual(contratoCreateDTO.getKmAtual());
        contrato.setKmInicial(contratoCreateDTO.getKmInicial());
        contrato.setLocadora(contratoCreateDTO.getLocadora());
        contrato.setMarca(contratoCreateDTO.getMarca());
        contrato.setModelo(contratoCreateDTO.getModelo());
        contrato.setNumeroContrato(contratoCreateDTO.getNumeroContrato());
        contrato.setOsCliente(contratoCreateDTO.getOsCliente());
        contrato.setPlaca(contratoCreateDTO.getPlaca());
        contrato.setValorAluguel(contratoCreateDTO.getValorAluguel());

        // Define observacoes
        StringBuilder observacoes = new StringBuilder();

        // Verifica se deve fazer revisão
        if (contrato.isFazerRevisao()) {
            observacoes.append("Necessário marcar a revisão");
        }

        // Verifica se kmExcedido
        if (contrato.isKmExcedido()) {
            if (observacoes.length() > 0) {
                observacoes.append(" | ");
            }
            observacoes.append("Km Excedido: ").append(contrato.getAcumuladoMes());
        } else {
            if (observacoes.length() > 0) {
                observacoes.append(" | ");
            }
            observacoes.append("Km Livre: ").append(contrato.getAcumuladoMes());
        }

        contrato.setObservacoes(observacoes.toString());

        ContratoModel savedContrato = contratoRepository.save(contrato);
        return convertToDTO(savedContrato);
    }

    //Método para conversões DTO
    private ContratoDTO convertToDTO(ContratoModel contrato) {
        return new ContratoDTO(
                contrato.getId(),
                contrato.getCondutorPrincipal(),
                contrato.getCondutorResponsavel(),
                contrato.getDataAtual(),
                contrato.getDataRegistro(),
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
                contrato.getQuantiaMeses(),
                contrato.getSaldoKm(),
                contrato.getAcumuladoMes(),
                contrato.getEntregaPropData(),
                contrato.getContadorRevisao(),
                contrato.getObservacoes()
        );
    }

    //Métodos para conversão para Model
    private ContratoModel convertToModel(ContratoDTO contratoDTO) {
        return new ContratoModel(
                contratoDTO.getId(),
                contratoDTO.getCondutorPrincipal(),
                contratoDTO.getCondutorResponsavel(),
                contratoDTO.getDataAtual(),
                contratoDTO.getDataRegistro(),
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
                contratoDTO.getQuantiaMeses(),
                contratoDTO.getSaldoKm(),
                contratoDTO.getAcumuladoMes(),
                contratoDTO.getEntregaPropData(),
                contratoDTO.getContadorRevisao(),
                contratoDTO.getObservacoes()
        );
    }
}
