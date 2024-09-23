package com.oksmart.kmcontrol.service;

import com.oksmart.kmcontrol.dto.AtualizarKmDTO;
import com.oksmart.kmcontrol.dto.ContratoCreateDTO;
import com.oksmart.kmcontrol.dto.ContratoDTO;
import com.oksmart.kmcontrol.model.ContratoModel;
import com.oksmart.kmcontrol.repository.ContratoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
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

        // Define dataRegistro a partir do DTO
        LocalDate dataRegistro = contratoCreateDTO.getDataRegistro();
        contrato.setDataRegistro(dataRegistro);

        // Calcula a quantidade de meses entre dataRegistro e dataAtual
        long quantiaMeses = ChronoUnit.MONTHS.between(dataRegistro, dataAtual);
        contrato.setQuantiaMeses((int) quantiaMeses);

        // Calcula kmMediaMensal
        int mesesParaCalculo = quantiaMeses == 0 ? 1 : (int) quantiaMeses;
        double kmMediaMensal = (double) contratoCreateDTO.getKmAtual() / mesesParaCalculo;
        contrato.setKmMediaMensal((long) kmMediaMensal);

        // Calcula contadorRevisao
        int contadorRevisao = contratoCreateDTO.getKmAtual() - contratoCreateDTO.getKmInicial();
        contrato.setContadorRevisao(contadorRevisao);

        // Verifica se deve fazer revisão
        contrato.setFazerRevisao(contadorRevisao > 10000);

        // Calcula kmIdeal
        int mesesParaKmIdeal = quantiaMeses == 0 ? 1 : (int) quantiaMeses;
        int kmIdeal = contratoCreateDTO.getFranquiaKm() * mesesParaKmIdeal;
        contrato.setKmIdeal(kmIdeal);

        // Calcula acumuladoMes
        long acumuladoMes = kmIdeal - contratoCreateDTO.getKmAtual();
        contrato.setAcumuladoMes(acumuladoMes);

        // Define kmExcedido com base no acumuladoMes
        contrato.setKmExcedido(acumuladoMes < 0);

        // Calcula saldoKm
        double saldoKm = (contratoCreateDTO.getValorAluguel()
                / contratoCreateDTO.getFranquiaKm()) * acumuladoMes;
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

    // Métodos para conversão para Model
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

    // Método para deletar um contrato pelo número do contrato
    public void deletarContrato(String numeroContrato) {
        ContratoModel contrato = contratoRepository.findByNumeroContrato(numeroContrato);
        if (contrato != null) {
            contratoRepository.delete(contrato);
        } else {
            throw new IllegalArgumentException("Contrato não encontrado para o número fornecido.");
        }
    }

    //Método para atualizar o km dos veículos
    public ContratoDTO atualizarKm(AtualizarKmDTO atualizarKmDTO) {
        // Busca todos os contratos com a placa fornecida
        List<ContratoModel> contratos = contratoRepository.findByPlaca(atualizarKmDTO.getPlaca());

        if (contratos.isEmpty()) {
            throw new IllegalArgumentException("Contrato não encontrado para a placa fornecida.");
        }

        // Pega o último contrato (primeiro da lista após ordenação)
        ContratoModel ultimoContrato = contratos.get(0);

        // Verifica se o kmAtual fornecido é maior que o kmAtual do último contrato
        if (atualizarKmDTO.getKmAtual() <= ultimoContrato.getKmAtual()) {
            throw new IllegalArgumentException("O Km Atual deve ser maior que: " + ultimoContrato.getKmAtual() + " KM.");
        }

        // Cria um novo registro com os dados fornecidos
        ContratoModel novoContrato = new ContratoModel();
        novoContrato.setPlaca(atualizarKmDTO.getPlaca());
        novoContrato.setKmAtual(atualizarKmDTO.getKmAtual());
        novoContrato.setDataAtual(LocalDate.now()); // Define a data atual

        // Replicando os dados do último contrato
        novoContrato.setCondutorPrincipal(ultimoContrato.getCondutorPrincipal());
        novoContrato.setCondutorResponsavel(ultimoContrato.getCondutorResponsavel());
        novoContrato.setDiarias(ultimoContrato.getDiarias());
        novoContrato.setFranquiaKm(ultimoContrato.getFranquiaKm());
        novoContrato.setKmInicial(ultimoContrato.getKmInicial());
        novoContrato.setLocadora(ultimoContrato.getLocadora());
        novoContrato.setMarca(ultimoContrato.getMarca());
        novoContrato.setModelo(ultimoContrato.getModelo());
        novoContrato.setNumeroContrato(ultimoContrato.getNumeroContrato());
        novoContrato.setOsCliente(ultimoContrato.getOsCliente());
        novoContrato.setValorAluguel(ultimoContrato.getValorAluguel());

        // Calcula a quantidade de meses entre dataRegistro e dataAtual
        long quantiaMeses = ChronoUnit.MONTHS.between(ultimoContrato.getDataRegistro(), LocalDate.now());
        novoContrato.setQuantiaMeses((int) quantiaMeses);

        // Salva o novo contrato no banco
        ContratoModel savedContrato = contratoRepository.save(novoContrato);
        return convertToDTO(savedContrato);
    }
}
