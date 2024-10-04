package com.oksmart.kmcontrol.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "contratos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContratoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "condutor_principal")
    private String condutorPrincipal;

    @Column(name = "condutor_responsavel")
    private String condutorResponsavel;

    @Column(name = "data_atual")
    private LocalDate dataAtual;

    @Column(name = "data_registro")
    private LocalDate dataRegistro;

    @Column(name = "data_vigencia")
    private LocalDate dataVigencia;

    @Column(name="data_substituicao")
    private LocalDate dataSubstituicao;

    @Column(name = "diarias")
    private int diarias;

    @Column(name = "franquia_km")
    private int franquiaKm;

    @Column(name = "km_atual")
    private int kmAtual;

    @Column(name = "km_inicial")
    private int kmInicial;

    @Column(name = "locadora")
    private String locadora;

    @Column(name = "marca")
    private String marca;

    @Column(name = "modelo")
    private String modelo;

    @Column(name = "numero_contrato")
    private String numeroContrato;

    @Column(name = "os_cliente")
    private String osCliente;

    @Column(name = "placa")
    private String placa;

    @Column(name = "valor_aluguel")
    private double valorAluguel;

    @Column(name = "fazer_revisao")
    private boolean fazerRevisao;

    @Column(name = "km_excedido")
    private boolean kmExcedido;

    @Column(name = "km_ideal")
    private long kmIdeal;

    @Column(name = "km_semana")
    private long kmSemana;

    @Column(name = "km_media_mensal")
    private long kmMediaMensal;

    @Column(name = "quantia_meses")
    private int quantiaMeses;

    @Column(name = "saldo_km")
    private double saldoKm;

    @Column(name = "acumulado_mes")
    private long acumuladoMes;

    @Column(name = "entrega_prop_data")
    private LocalDate entregaPropData;

    @Column(name = "contador_revisao")
    private int contadorRevisao;

    @Column(name = "observacoes")
    private String observacoes;
}
