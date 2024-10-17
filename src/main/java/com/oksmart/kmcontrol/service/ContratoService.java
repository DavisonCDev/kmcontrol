package com.oksmart.kmcontrol.service;

import com.oksmart.kmcontrol.dto.*;
import com.oksmart.kmcontrol.model.ContratoModel;
import com.oksmart.kmcontrol.repository.ContratoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.HashMap;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContratoService {

    @Autowired
    private ContratoRepository contratoRepository;

    // Método para listar todos os contratos
    public List<ContratoDTO> listarTodos() {
        return contratoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Método para criar um contrato
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
        System.out.println("Data atual: " + dataAtual);

        // Define dataRegistro a partir do DTO
        LocalDate dataRegistro = contratoCreateDTO.getDataRegistro();
        contrato.setDataRegistro(dataRegistro);
        System.out.println("Data de registro: " + dataRegistro);

        // Calcular a quantidade de meses entre dataRegistro e dataAtual
        long qtMesesCont = ChronoUnit.MONTHS.between(dataRegistro, dataAtual);
        contrato.setQtMesesCont((int) qtMesesCont);
        contrato.setQtMesesVeic((int) qtMesesCont);
        System.out.println("Quantidade de meses entre dataRegistro e dataAtual: " + qtMesesCont);

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
        System.out.println("KM ideal: " + kmIdeal);

        // Calcula acumuladoMes
        long acumuladoMes = kmIdeal - contratoCreateDTO.getKmAtual();
        contrato.setAcumuladoMes(acumuladoMes);
        System.out.println("Acumulado do mês: " + acumuladoMes);

        // Define kmExcedido com base no acumuladoMes
        contrato.setKmExcedido(acumuladoMes < 0);
        System.out.println("KM excedido? " + contrato.isKmExcedido());

        // Calcula saldoKm
        double saldoKm = (contratoCreateDTO.getValorAluguel() / contratoCreateDTO.getFranquiaKm()) * acumuladoMes;
        contrato.setSaldoKm(saldoKm);
        System.out.println("Saldo KM: " + saldoKm);

        // Resto do código...
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
        System.out.println("Contrato salvo com sucesso: " + savedContrato);
        return convertToDTO(savedContrato);
    }

    // Método para conversões DTO
    private ContratoDTO convertToDTO(ContratoModel contrato) {
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

    // Método para deletar um contrato pelo número do contrato
    public void deletarContrato(String numeroContrato) {
        List<ContratoModel> contratos = contratoRepository.findByNumeroContrato(numeroContrato);
        if (contratos.isEmpty()) {
            throw new IllegalArgumentException("Contrato não encontrado para o número fornecido.");
        }
        contratoRepository.deleteAll(contratos);
    }

    // Método para atualizar o km dos veículos
    public ContratoDTO atualizarKm(AtualizarKmDTO atualizarKmDTO) {
        List<ContratoModel> contratos = contratoRepository.findByPlaca(atualizarKmDTO.getPlaca());
        if (contratos.isEmpty()) {
            throw new IllegalArgumentException("Contrato não encontrado para a placa fornecida.");
        }

        ContratoModel ultimoContrato = contratos.get(0);
        if (atualizarKmDTO.getKmAtual() <= ultimoContrato.getKmAtual()) {
            throw new IllegalArgumentException("O Km Atual deve ser maior que: " + ultimoContrato.getKmAtual() + " KM.");
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
            throw new IllegalArgumentException("Data de registro do último contrato não pode ser nula.");
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
        int contadorRevisao = atualizarKmDTO.getKmAtual() - ultimoContrato.getKmAtual() + ultimoContrato.getContadorRevisao();
        novoContrato.setContadorRevisao(contadorRevisao);
        novoContrato.setFazerRevisao(contadorRevisao > 10000);

        // Cálculo do kmIdeal
        long kmIdeal = (novoContrato.getFranquiaKm() * (qtMesesVeic+1)) + novoContrato.getKmInicial();
        novoContrato.setKmIdeal(kmIdeal);

        // Cálculo do acumuladoMes
        long acumuladoMes = kmIdeal - atualizarKmDTO.getKmAtual();
        novoContrato.setAcumuladoMes(acumuladoMes);

        // Cálculo do saldoKm
        double saldoKm = (double) ultimoContrato.getValorAluguel() / ultimoContrato.getFranquiaKm() * acumuladoMes;
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
        return convertToDTO(savedContrato);
    }

    // Método para fazer revisão de um contrato
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
        return convertToDTO(savedContrato);
    }

    //Método para listar registros por número de contrato.
    public List<ContratoDTO> listarUltimosContratos() {
        List<ContratoModel> ultimosContratos = contratoRepository.findUltimosContratos();
        return ultimosContratos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    //Método para substituir veículos
    public ContratoDTO substituirVeiculo(SubstituirVeiculoDTO substituirVeiculoDTO) {
        // Busca o último contrato com o número do contrato fornecido
        List<ContratoModel> contratos = contratoRepository.findByNumeroContrato(substituirVeiculoDTO.getNumeroContrato());

        if (contratos.isEmpty()) {
            throw new IllegalArgumentException("Contrato não encontrado para o número fornecido.");
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
        return convertToDTO(savedContrato);
    }
}
