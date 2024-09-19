package com.oksmart.kmcontrol.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContratoCreateDTO {
    private String condutorPrincipal;
    private String condutorResponsavel;
    private LocalDate dataRegistro;
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
}
