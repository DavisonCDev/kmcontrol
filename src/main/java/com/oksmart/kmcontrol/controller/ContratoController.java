package com.oksmart.kmcontrol.controller;

import com.oksmart.kmcontrol.dto.*;
import com.oksmart.kmcontrol.service.ContratoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contratos")
public class ContratoController {

    @Autowired
    private ContratoService contratoService;

    //Endpoint para listar contratos.
    @GetMapping
    public List<ContratoDTO> listarContratos() {
        return contratoService.listarTodos();
    }

    //Endpoint para criar contrato
    @PostMapping
    public ContratoDTO criarContrato(@RequestBody ContratoCreateDTO contratoCreateDTO) {
        return contratoService.criarContrato(contratoCreateDTO);
    }

    // Endpoint para deletar contrato
    @DeleteMapping
    public void deletarContrato(@RequestBody ContratoDeleteDTO contratoDeleteDTO) {
        contratoService.deletarContrato(contratoDeleteDTO.getNumeroContrato());
    }

    //Endpoint para atualizar km
    @PostMapping("/atualizar-km")
    public ContratoDTO atualizarKm(@RequestBody AtualizarKmDTO atualizarKmDTO) {
        return contratoService.atualizarKm(atualizarKmDTO);
    }

    //Endpoint para fazer a revisão dos veículos
    @PostMapping("/fazer-revisao")
    public ContratoDTO fazerRevisao(@RequestBody FazerRevisaoDTO fazerRevisaoDTO) {
        return contratoService.fazerRevisao(fazerRevisaoDTO);
    }

    //Endpoint para listar ultimos registros por numero de contrato
    @GetMapping("/ultimos")
    public List<ContratoDTO> listarUltimosContratos() {
        return contratoService.listarUltimosContratos();
    }

}
