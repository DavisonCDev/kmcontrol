package com.oksmart.kmcontrol.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class AtualizarKmDTO {
    @NotBlank(message = "Placa é obrigatória.")
    private String placa;

    @NotNull(message = "KM atual é obrigatório.")
    private int kmAtual;
}
