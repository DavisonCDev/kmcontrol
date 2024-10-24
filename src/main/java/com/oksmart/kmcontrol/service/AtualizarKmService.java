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
    private ContratoRepository contratoRepository;

    @Autowired
    private ContratoConverter contratoConverter;

    public ContratoDTO atualizarKm(AtualizarKmDTO atualizarKmDTO) {
        List<ContratoModel> contratos = contratoRepository.findByPlaca(atualizarKmDTO.getPlaca());
        if (contratos.isEmpty()) {
            throw new ServiceException("Contrato não encontrado para a placa fornecida.");
        }

        ContratoModel ultimoContrato = contratos.get(0);
        if (atualizarKmDTO.getKmAtual() <= ultimoContrato.getKmAtual()) {
            throw new ServiceException("O Km Atual deve ser maior que: "
                    + ultimoContrato.getKmAtual() + " KM.");
        }

        ContratoModel novoContrato = new ContratoModel();
        novoContrato.setPlaca(atualizarKmDTO.getPlaca());
        novoContrato.setKmAtual(atualizarKmDTO.getKmAtual());
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

        if (ultimoContrato.getDataRegistro() == null) {
            throw new ServiceException("Data de registro do último contrato não pode ser nula.");
        }

        long qtMesesCont = ChronoUnit.MONTHS.between(ultimoContrato.getDataRegistro(), LocalDate.now());

        // Usando dataRegistro se dataSubstituicao for nula
        LocalDate dataReferencia = (ultimoContrato.getDataSubstituicao() != null)
                ? ultimoContrato.getDataSubstituicao()
                : ultimoContrato.getDataRegistro();

        long qtMesesVeic = ChronoUnit.MONTHS.between(dataReferencia, LocalDate.now());

        novoContrato.setQtMesesCont((int) qtMesesCont);
        novoContrato.setQtMesesVeic((int) qtMesesVeic);

        long totalDiasCont = ultimoContrato.getDiarias() * qtMesesCont;
        // Cálculo da vigência do contrato
        LocalDate dataVigencia = ultimoContrato.getDataRegistro().plusDays(totalDiasCont);

        // Verificar se a dataVigencia é anterior à dataAtual
        if (dataVigencia.isBefore(ultimoContrato.getDataAtual())) {
            dataVigencia = dataVigencia.plusDays(ultimoContrato.getDiarias());
        }

        novoContrato.setDataVigencia(dataVigencia);

        int kmPercorridos = atualizarKmDTO.getKmAtual() - ultimoContrato.getKmInicial();
        double kmMediaMensal = (qtMesesCont == 0 ? 1 : qtMesesCont); // Considera 1 se qtMesesCont for 0
        double media = (double) kmPercorridos / kmMediaMensal;
        novoContrato.setKmMediaMensal((long) media);
        novoContrato.setKmExcedido(media > ultimoContrato.getFranquiaKm());

        // Cálculo do contadorRevisao
        int contadorRevisao = atualizarKmDTO.getKmAtual() -
                ultimoContrato.getKmAtual() + ultimoContrato.getContadorRevisao();
        novoContrato.setContadorRevisao(contadorRevisao);
        novoContrato.setFazerRevisao(contadorRevisao > 10000);

        // Cálculo do kmIdeal
        long kmIdeal = (novoContrato.getFranquiaKm() * (qtMesesVeic+1)) + novoContrato.getKmInicial();
        novoContrato.setKmIdeal(kmIdeal);

        // Cálculo do acumuladoMes
        long acumuladoMes = kmIdeal - atualizarKmDTO.getKmAtual();
        novoContrato.setAcumuladoMes(acumuladoMes);

        // Cálculo do saldoKm
        double saldoKm = (double) ultimoContrato.getValorAluguel() / ultimoContrato.getFranquiaKm()
                * acumuladoMes;
        novoContrato.setSaldoKm(saldoKm);

        // Verifica se acumuladoMes é menor que 0 para definir kmExcedido
        novoContrato.setKmExcedido(acumuladoMes < 0);

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
