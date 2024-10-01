package com.oksmart.kmcontrol.dto;

import lombok.Data;

import java.time.LocalDate;


// DTO para substituição de veículo
@Data
public class SubstituirVeiculoDTO {
    private String numeroContrato;
    private String placa;
    private LocalDate dataRegistro;
    private int kmInicial;
    private int kmAtual;
    private String modelo;
    private String marca;
}
