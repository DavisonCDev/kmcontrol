package com.oksmart.kmcontrol.service;

import com.oksmart.kmcontrol.dto.ContratoCreateDTO;
import com.oksmart.kmcontrol.dto.ContratoDTO;
import com.oksmart.kmcontrol.model.ContratoModel;
import com.oksmart.kmcontrol.repository.ContratoRepository;
import com.oksmart.kmcontrol.service.converter.ContratoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class CriarContratoService {

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private ContratoConverter contratoConverter;

    public ContratoDTO criarContrato(ContratoCreateDTO contratoCreateDTO) {
        // Verificar se já existe um contrato com a mesma placa ou número de contrato
        boolean exists = contratoRepository.findAll().stream().anyMatch(c ->
                c.getPlaca().equals(contratoCreateDTO.getPlaca()) ||
                        c.getNumeroContrato().equals(contratoCreateDTO.getNumeroContrato())
        );

        if (exists) {
            throw new IllegalArgumentException("Já existe um contrato com " +
                    "a mesma placa ou número de contrato.");
        }

        ContratoModel contrato = new ContratoModel();
        contrato.setCondutorPrincipal(contratoCreateDTO.getCondutorPrincipal());
        contrato.setCondutorResponsavel(contratoCreateDTO.getCondutorResponsavel());

        // Define o campo dataAtual como a data atual
        LocalDate dataAtual = LocalDate.now();
        contrato.setDataAtual(dataAtual);

        // Define dataRegistro a partir do DTO
        LocalDate dataRegistro = contratoCreateDTO.getDataRegistro();
        contrato.setDataRegistro(dataRegistro);

        // Calcular a quantidade de meses entre dataRegistro e dataAtual
        long qtMesesCont = ChronoUnit.MONTHS.between(dataRegistro, dataAtual);
        contrato.setQtMesesCont((int) qtMesesCont);
        contrato.setQtMesesVeic((int) qtMesesCont);

        long totalDias = contratoCreateDTO.getDiarias() * contrato.getQtMesesVeic();
        LocalDate dataVigencia = dataRegistro.plusDays(totalDias);
        System.out.println("Total de dias calculados: " + totalDias + " | Data de vigência inicial: " + dataVigencia);

        // Verificar se a dataVigencia é anterior à dataAtual
        if (dataVigencia.isBefore(dataAtual)) {
            dataVigencia = dataVigencia.plusDays(contratoCreateDTO.getDiarias());
            System.out.println("Data de vigência foi ajustada para: " + dataVigencia);
        }

        contrato.setDataVigencia(dataVigencia);

        // Calcula kmMediaMensal
        int mesesParaCalculo = qtMesesCont == 0 ? 1 : (int) qtMesesCont;
        double kmMediaMensal = (double) contratoCreateDTO.getKmAtual() / mesesParaCalculo;
        contrato.setKmMediaMensal((long) kmMediaMensal);
        System.out.println("KM médio mensal: " + kmMediaMensal);

        // Calcula contadorRevisao
        int contadorRevisao = contratoCreateDTO.getKmAtual() - contratoCreateDTO.getKmInicial();
        contrato.setContadorRevisao(contadorRevisao);
        System.out.println("Contador de revisão: " + contadorRevisao);

        // Verifica se deve fazer revisão
        contrato.setFazerRevisao(contadorRevisao > 10000);
        System.out.println("Necessário fazer revisão? " + contrato.isFazerRevisao());

        // Calcula kmIdeal
        int kmIdeal = (contratoCreateDTO.getFranquiaKm() * ((int) qtMesesCont+1)) + contratoCreateDTO.getKmInicial();
        contrato.setKmIdeal(kmIdeal);

        // Calcula acumuladoMes
        long acumuladoMes = kmIdeal - contratoCreateDTO.getKmAtual();
        contrato.setAcumuladoMes(acumuladoMes);

        // Define kmExcedido com base no acumuladoMes
        contrato.setKmExcedido(acumuladoMes < 0);

        // Calcula saldoKm
        double saldoKm = (contratoCreateDTO.getValorAluguel() / contratoCreateDTO.getFranquiaKm()) * acumuladoMes;
        contrato.setSaldoKm(saldoKm);
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

        // Define observações
        StringBuilder observacoes = new StringBuilder();

        if (contrato.isFazerRevisao()) {
            observacoes.append("Necessário marcar a revisão");
        }

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
        return contratoConverter.convertToDTO(savedContrato);
    }
}
