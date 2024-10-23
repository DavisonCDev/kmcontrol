package com.oksmart.kmcontrol.controller;

import com.oksmart.kmcontrol.dto.*;
import com.oksmart.kmcontrol.exception.ServiceException;
import com.oksmart.kmcontrol.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.oksmart.kmcontrol.exception.ServiceException;

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

    // Endpoint para listar contratos
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ContratoDTO>>> listarContratos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ContratoDTO> contratos = (Page<ContratoDTO>) listarTodosService.listarTodos(page, size);
        return ResponseEntity.ok(new ApiResponse<>("success", "Contratos listados com sucesso.", contratos, null));
    }

    // Endpoint para criar contrato
    @PostMapping
    public ResponseEntity<ApiResponse<ContratoDTO>> criarContrato(@Valid @RequestBody ContratoCreateDTO contratoCreateDTO) {
        try {
            ContratoDTO contratoDTO = criarContratoService.criarContrato(contratoCreateDTO);
            return ResponseEntity.ok(new ApiResponse<>("success", "Contrato criado com sucesso.", contratoDTO, null));
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("error", e.getErrorMessage(), null, null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("error", "Erro ao criar contrato: " + e.getMessage(), null, null));
        }
    }

    // Endpoint para deletar contrato
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deletarContrato(@Valid @RequestBody ContratoDeleteDTO contratoDeleteDTO) {
        try {
            deletarContratoService.deletarContrato(contratoDeleteDTO.getNumeroContrato());
            return ResponseEntity.ok(new ApiResponse<>("success", "Contrato deletado com sucesso.", null, null));
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("error", e.getErrorMessage(), null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("error", "Erro ao deletar contrato.", null, null));
        }
    }

    // Endpoint para atualizar km
    @PostMapping("/atualizar-km")
    public ResponseEntity<ApiResponse<ContratoDTO>> atualizarKm(@Valid @RequestBody AtualizarKmDTO atualizarKmDTO) {
        try {
            ContratoDTO contratoDTO = atualizarKmService.atualizarKm(atualizarKmDTO);
            return ResponseEntity.ok(new ApiResponse<>("success", "KM atualizado com sucesso.", contratoDTO, null));
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("error", e.getErrorMessage(), null, null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("error", "Erro ao atualizar KM: " + e.getMessage(), null, null));
        }
    }

    // Endpoint para fazer a revisão dos veículos
    @PostMapping("/fazer-revisao")
    public ResponseEntity<ApiResponse<ContratoDTO>> fazerRevisao(@Valid @RequestBody FazerRevisaoDTO fazerRevisaoDTO) {
        try {
            ContratoDTO contratoDTO = fazerRevisaoService.fazerRevisao(fazerRevisaoDTO);
            return ResponseEntity.ok(new ApiResponse<>("success", "Revisão realizada com sucesso.", contratoDTO, null));
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("error", e.getErrorMessage(), null, null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("error", "Erro ao fazer revisão: " + e.getMessage(), null, null));
        }
    }

    // Endpoint para listar últimos registros por número de contrato
    @GetMapping("/ultimos")
    public ResponseEntity<ApiResponse<List<ContratoDTO>>> listarUltimosContratos() {
        List<ContratoDTO> contratos = listarContratosService.listarUltimosContratos();
        return ResponseEntity.ok(new ApiResponse<>("success", "Últimos contratos listados com sucesso.", contratos, null));
    }

    // Endpoint para substituir veículos
    @PostMapping("/substituirVeiculo")
    public ResponseEntity<ApiResponse<ContratoDTO>> substituirVeiculo(@Valid @RequestBody SubstituirVeiculoDTO substituirVeiculoDTO) {
        try {
            ContratoDTO contratoDTO = substituirVeiculoService.substituirVeiculo(substituirVeiculoDTO);
            return ResponseEntity.ok(new ApiResponse<>("success", "Veículo substituído com sucesso.", contratoDTO, null));
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("error", e.getErrorMessage(), null, null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("error", "Erro ao substituir veículo: " + e.getMessage(), null, null));
        }
    }
}

