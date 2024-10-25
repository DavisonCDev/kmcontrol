package com.oksmart.kmcontrol.service;

import com.oksmart.kmcontrol.dto.AtualizarKmDTO;
import com.oksmart.kmcontrol.dto.ContratoDTO;
import com.oksmart.kmcontrol.exception.ServiceException;
import com.oksmart.kmcontrol.model.ContratoModel;
import com.oksmart.kmcontrol.repository.ContratoRepository;
import com.oksmart.kmcontrol.service.converter.ContratoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AtualizarKmService {

    @Autowired
    ContratoRepository contratoRepository;

    @Autowired
    ContratoConverter contratoConverter;

    public ContratoDTO atualizarKm(AtualizarKmDTO atualizarKmDTO) {
        List<ContratoModel> contratos = contratoRepository.findByPlaca(atualizarKmDTO.getPlaca());
        if (contratos == null || contratos.isEmpty()) {
            throw new ServiceException("Contrato não encontrado para a placa fornecida.");
        }

        ContratoModel ultimoContrato = contratos.get(0);
        if (atualizarKmDTO.getKmAtual() <= ultimoContrato.getKmAtual()) {
            throw new ServiceException("O Km Atual deve ser maior que: " + ultimoContrato.getKmAtual() + " KM.");
        }

        // Criação do novo contrato
        ContratoModel novoContrato = criarNovoContrato(ultimoContrato, atualizarKmDTO.getKmAtual());

        // Cálculo da data de vigência
        calcularDataVigencia(novoContrato, ultimoContrato);

        // Cálculo de km ideal, acumulado e saldo
        calcularKm(novoContrato, ultimoContrato, atualizarKmDTO.getKmAtual());

        // Define observações
        definirObservacoes(novoContrato);

        ContratoModel savedContrato = contratoRepository.save(novoContrato);
        return contratoConverter.convertToDTO(savedContrato);
    }

    private ContratoModel criarNovoContrato(ContratoModel ultimoContrato, int kmAtual) {
        ContratoModel novoContrato = new ContratoModel();
        novoContrato.setPlaca(ultimoContrato.getPlaca());
        novoContrato.setKmAtual(kmAtual);
        novoContrato.setDataAtual(LocalDate.now());

        novoContrato.setCondutorPrincipal(ultimoContrato.getCondutorPrincipal());
        novoContrato.setCondutorResponsavel(ultimoContrato.getCondutorResponsavel());
        novoContrato.setDiarias(ultimoContrato.getDiarias());
        novoContrato.setDataRegistro(ultimoContrato.getDataRegistro());
        novoContrato.setFranquiaKm(ultimoContrato.getFranquiaKm());
        novoContrato.setKmInicial(ultimoContrato.getKmInicial());
        novoContrato.setLocadora(ultimoContrato.getLocadora());
        novoContrato.setMarca(ultimoContrato.getMarca());
        novoContrato.setModelo(ultimoContrato.getModelo());
        novoContrato.setNumeroContrato(ultimoContrato.getNumeroContrato());
        novoContrato.setOsCliente(ultimoContrato.getOsCliente());
        novoContrato.setValorAluguel(ultimoContrato.getValorAluguel());
        novoContrato.setDataSubstituicao(ultimoContrato.getDataSubstituicao());

        return novoContrato;
    }

    private void calcularDataVigencia(ContratoModel novoContrato, ContratoModel ultimoContrato) {
        LocalDate dataReferencia = (ultimoContrato.getDataSubstituicao() != null)
                ? ultimoContrato.getDataSubstituicao()
                : ultimoContrato.getDataRegistro();

        long qtMesesCont = ChronoUnit.MONTHS.between(dataReferencia, LocalDate.now());
        long totalDiasCont = ultimoContrato.getDiarias() * qtMesesCont;
        LocalDate dataVigencia = ultimoContrato.getDataRegistro().plusDays(totalDiasCont);

        // Verificar se a dataVigencia é anterior à dataAtual
        if (dataVigencia.isBefore(novoContrato.getDataAtual())) {
            dataVigencia = dataVigencia.plusDays(ultimoContrato.getDiarias());
        }

        novoContrato.setDataVigencia(dataVigencia);
    }

    private void calcularKm(ContratoModel novoContrato, ContratoModel ultimoContrato, int kmAtual) {
        long kmPercorridos = kmAtual - ultimoContrato.getKmInicial();
        long qtMesesCont = ChronoUnit.MONTHS.between(ultimoContrato.getDataRegistro(), LocalDate.now());

        // Evitar divisão por zero
        double kmMediaMensal = (qtMesesCont == 0 ? 1 : qtMesesCont);
        double media = (double) kmPercorridos / kmMediaMensal;

        novoContrato.setKmMediaMensal((long) media);
        novoContrato.setKmExcedido(media > ultimoContrato.getFranquiaKm());

        // Cálculo do contadorRevisao
        int contadorRevisao = kmAtual - ultimoContrato.getKmAtual() + ultimoContrato.getContadorRevisao();
        novoContrato.setContadorRevisao(contadorRevisao);
        novoContrato.setFazerRevisao(contadorRevisao > 10000);

        // Cálculo do kmIdeal
        long kmIdeal = (novoContrato.getFranquiaKm() * (qtMesesCont + 1)) + novoContrato.getKmInicial();
        novoContrato.setKmIdeal(kmIdeal);

        // Cálculo do acumuladoMes
        long acumuladoMes = kmIdeal - kmAtual;
        novoContrato.setAcumuladoMes(acumuladoMes);

        // Cálculo do saldoKm
        double saldoKm = (double) ultimoContrato.getValorAluguel() / ultimoContrato.getFranquiaKm() * acumuladoMes;
        novoContrato.setSaldoKm(saldoKm);

        // Verifica se acumuladoMes é menor que 0 para definir kmExcedido
        novoContrato.setKmExcedido(acumuladoMes < 0);
    }

    private void definirObservacoes(ContratoModel novoContrato) {
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
    }
}
