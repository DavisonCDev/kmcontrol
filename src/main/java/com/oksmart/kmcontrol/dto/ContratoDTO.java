package com.oksmart.kmcontrol.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContratoDTO {
    private Long id;
    private String condutorPrincipal;
    private String condutorResponsavel;
    private LocalDate dataAtual;
    private LocalDate dataRegistro;
    private LocalDate dataVigencia;
    private LocalDate dataSubstituicao;
    private int diarias;
    private int franquiaKm;
    private int kmAtual;
    private int kmInicial;
    private String locadora;
    private String marca;
    private String modelo;
    private String numeroContrato;
    private String osCliente;
    private String placa;
    private double valorAluguel;
    private boolean fazerRevisao;
    private boolean kmExcedido;
    private long kmIdeal;
    private long kmSemana;
    private long kmMediaMensal;
    private int qtMesesVeic;
    private int qtMesesCont;
    private double saldoKm;
    private long acumuladoMes;
    private LocalDate entregaPropData;
    private int contadorRevisao;
    private String observacoes;
}
