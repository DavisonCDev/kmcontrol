package com.oksmart.kmcontrol.controller;

import com.oksmart.kmcontrol.dto.*;
import com.oksmart.kmcontrol.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contratos")
public class ContratoController {
    @Autowired
    private CriarContratoService criarContratoService;

    @Autowired
    private ListarTodosService listarTodosService;

    @Autowired
    private DeletarContratoService deletarContratoService;

    @Autowired
    private AtualizarKmService atualizarKmService;

    @Autowired
    private FazerRevisaoService fazerRevisaoService;

    @Autowired
    private SubstituirVeiculoService substituirVeiculoService;

    @Autowired
    private ListarContratosService listarContratosService;


    //Endpoint para listar contratos.
    @GetMapping
    public List<ContratoDTO> listarContratos() {
        return listarTodosService.listarTodos();
    }

    //Endpoint para criar contrato
    @PostMapping
    public ContratoDTO criarContrato(@RequestBody ContratoCreateDTO contratoCreateDTO) {
        return criarContratoService.criarContrato(contratoCreateDTO);
    }

    // Endpoint para deletar contrato
    @DeleteMapping
    public void deletarContrato(@RequestBody ContratoDeleteDTO contratoDeleteDTO) {
        deletarContratoService.deletarContrato(contratoDeleteDTO.getNumeroContrato());
    }

    //Endpoint para atualizar km
    @PostMapping("/atualizar-km")
    public ContratoDTO atualizarKm(@RequestBody AtualizarKmDTO atualizarKmDTO) {
        return atualizarKmService.atualizarKm(atualizarKmDTO);
    }

    //Endpoint para fazer a revisão dos veículos
    @PostMapping("/fazer-revisao")
    public ContratoDTO fazerRevisao(@RequestBody FazerRevisaoDTO fazerRevisaoDTO) {
        return fazerRevisaoService.fazerRevisao(fazerRevisaoDTO);
    }

    //Endpoint para listar ultimos registros por numero de contrato
    @GetMapping("/ultimos")
    public List<ContratoDTO> listarUltimosContratos() {
        return listarContratosService.listarUltimosContratos();
    }

    //Endpoint para substituir veículos
    @PostMapping("/substituirVeiculo")
    public ResponseEntity<ContratoDTO> substituirVeiculo(@RequestBody SubstituirVeiculoDTO substituirVeiculoDTO) {
        ContratoDTO contratoDTO = substituirVeiculoService.substituirVeiculo(substituirVeiculoDTO);
        return ResponseEntity.ok(contratoDTO);
    }
}
