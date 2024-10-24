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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/contratos")
@Tag(name = "Contratos", description = "Operações relacionadas aos contratos")
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

    @Operation(summary = "Listar contratos", description = "Retorna uma lista paginada de contratos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contratos listados com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @GetMapping
    public ResponseEntity<ApiResponseDTO<Page<ContratoDTO>>> listarContratos(
            @Parameter(description = "Número da página para paginacao", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página", example = "10")
            @RequestParam(defaultValue = "10") int size) {

        Page<ContratoDTO> contratos = listarTodosService.listarTodos(page, size);
        return ResponseEntity.ok(new ApiResponseDTO<>(
                "success", "Contratos listados com sucesso.", contratos, null));
    }

    @Operation(summary = "Criar contrato", description = "Cria um novo contrato.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contrato criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro ao criar contrato.")
    })
    @PostMapping
    public ResponseEntity<ApiResponseDTO<ContratoDTO>> criarContrato(
            @Valid @RequestBody ContratoCreateDTO contratoCreateDTO) {

        try {
            ContratoDTO contratoDTO = criarContratoService.criarContrato(contratoCreateDTO);
            return ResponseEntity.ok(new ApiResponseDTO<>(
                    "success", "Contrato criado com sucesso.", contratoDTO, null));
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDTO<>("error", e.getErrorMessage(), null, null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponseDTO<>("error", "Erro ao criar contrato: " + e.getMessage(), null, null));
        }
    }

    @Operation(summary = "Deletar contrato", description = "Deleta um contrato existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contrato deletado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro ao deletar contrato."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @DeleteMapping
    public ResponseEntity<ApiResponseDTO<Void>> deletarContrato(
            @Valid @RequestBody ContratoDeleteDTO contratoDeleteDTO) {

        try {
            deletarContratoService.deletarContrato(contratoDeleteDTO.getNumeroContrato());
            return ResponseEntity.ok(new ApiResponseDTO<>("success", "Contrato deletado com sucesso.", null, null));
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDTO<>("error", e.getErrorMessage(), null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>("error", "Erro ao deletar contrato.", null, null));
        }
    }

    @Operation(summary = "Atualizar KM", description = "Atualiza a quilometragem de um contrato.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "KM atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro ao atualizar KM.")
    })
    @PostMapping("/atualizar-km")
    public ResponseEntity<ApiResponseDTO<ContratoDTO>> atualizarKm(
            @Valid @RequestBody AtualizarKmDTO atualizarKmDTO) {

        try {
            ContratoDTO contratoDTO = atualizarKmService.atualizarKm(atualizarKmDTO);
            return ResponseEntity.ok(new ApiResponseDTO<>("success", "KM atualizado com sucesso.", contratoDTO, null));
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDTO<>("error", e.getErrorMessage(), null, null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponseDTO<>("error", "Erro ao atualizar KM: " + e.getMessage(), null, null));
        }
    }

    @Operation(summary = "Fazer revisão", description = "Realiza a revisão de um veículo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Revisão realizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro ao fazer revisão.")
    })
    @PostMapping("/fazer-revisao")
    public ResponseEntity<ApiResponseDTO<ContratoDTO>> fazerRevisao(
            @Valid @RequestBody FazerRevisaoDTO fazerRevisaoDTO) {

        try {
            ContratoDTO contratoDTO = fazerRevisaoService.fazerRevisao(fazerRevisaoDTO);
            return ResponseEntity.ok(new ApiResponseDTO<>("success", "Revisão realizada com sucesso.", contratoDTO, null));
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDTO<>("error", e.getErrorMessage(), null, null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponseDTO<>("error", "Erro ao fazer revisão: " + e.getMessage(), null, null));
        }
    }

    @Operation(summary = "Listar últimos contratos", description = "Retorna os últimos contratos registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Últimos contratos listados com sucesso.")
    })
    @GetMapping("/ultimos")
    public ResponseEntity<ApiResponseDTO<List<ContratoDTO>>> listarUltimosContratos() {
        List<ContratoDTO> contratos = listarContratosService.listarUltimosContratos();
        return ResponseEntity.ok(new ApiResponseDTO<>("success", "Últimos contratos listados com sucesso.", contratos, null));
    }

    @Operation(summary = "Substituir veículo", description = "Substitui um veículo de um contrato.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veículo substituído com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro ao substituir veículo.")
    })
    @PostMapping("/substituirVeiculo")
    public ResponseEntity<ApiResponseDTO<ContratoDTO>> substituirVeiculo(
            @Valid @RequestBody SubstituirVeiculoDTO substituirVeiculoDTO) {

        try {
            ContratoDTO contratoDTO = substituirVeiculoService.substituirVeiculo(substituirVeiculoDTO);
            return ResponseEntity.ok(new ApiResponseDTO<>("success", "Veículo substituído com sucesso.", contratoDTO, null));
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDTO<>("error", e.getErrorMessage(), null, null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponseDTO<>("error", "Erro ao substituir veículo: " + e.getMessage(), null, null));
        }
    }
}
