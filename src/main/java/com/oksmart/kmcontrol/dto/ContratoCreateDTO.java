package com.oksmart.kmcontrol.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContratoCreateDTO {
    @NotBlank(message = "Condutor principal é obrigatório.")
    private String condutorPrincipal;

    @NotBlank(message = "Condutor responsável é obrigatório.")
    private String condutorResponsavel;

    @NotNull(message = "Data de registro é obrigatória.")
    private LocalDate dataRegistro;

    private LocalDate dataVigencia;

    @NotNull(message = "Número de diárias é obrigatório.")
    private int diarias;

    @NotNull(message = "Franquia de KM é obrigatória.")
    private int franquiaKm;

    @NotNull(message = "KM atual é obrigatório.")
    private int kmAtual;

    @NotNull(message = "KM inicial é obrigatório.")
    private int kmInicial;

    @NotBlank(message = "Locadora é obrigatória.")
    private String locadora;

    @NotBlank(message = "Marca é obrigatória.")
    private String marca;

    @NotBlank(message = "Modelo é obrigatório.")
    private String modelo;

    @NotBlank(message = "Número do contrato é obrigatório.")
    private String numeroContrato;

    @NotBlank(message = "OS Cliente é obrigatória.")
    private String osCliente;

    @NotBlank(message = "Placa é obrigatória.")
    private String placa;

    @NotNull(message = "Valor do aluguel é obrigatório.")
    private double valorAluguel;
}
