package com.manutech.ManuTech.controller;

import com.manutech.ManuTech.dto.OrdemServicoRequestDTO;
import com.manutech.ManuTech.dto.OrdemServicoResponseDTO;
import com.manutech.ManuTech.model.Prioridade;
import com.manutech.ManuTech.model.StatusOrdem;
import com.manutech.ManuTech.service.OrdemServicoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordens")
public class OrdemServicoController {

    private final OrdemServicoService service;

    public OrdemServicoController(OrdemServicoService service) {
        this.service = service;
    }

    @GetMapping
    public List<OrdemServicoResponseDTO> listar() {
        return service.listarOrdens();
    }

    @GetMapping("/codigo")
    public List<OrdemServicoResponseDTO> listarPorMaquina(@RequestParam String codigoIdentificador) {
        return service.listarOrdensPorMaquina(codigoIdentificador);
    }

    @GetMapping("/setor")
    public List<OrdemServicoResponseDTO> listarPorSetor(@RequestParam String nomeSetor) {
        return service.listarOrdensPorSetor(nomeSetor);
    }

    @GetMapping("/{idOrdem}")
    public OrdemServicoResponseDTO buscarPorId(@PathVariable Long idOrdem) {
        return service.buscarPorId(idOrdem);
    }

    @GetMapping("/titulo")
    public List<OrdemServicoResponseDTO> buscarPorTitulo(@RequestParam String titulo) {
        return service.buscarPorTitulo(titulo);
    }

    @GetMapping("/prioridade")
    public List<OrdemServicoResponseDTO> buscarPorPrioridade(@RequestParam Prioridade prioridade) {
        return service.buscarPorPrioridade(prioridade);
    }

    @GetMapping("/status")
    public List<OrdemServicoResponseDTO> buscarPorStatus(@RequestParam StatusOrdem status) {
        return service.buscarPorStatus(status);
    }

    @PostMapping
    public OrdemServicoResponseDTO salvar(@RequestBody @Valid OrdemServicoRequestDTO dto) {
        return service.salvarOrdem(dto);
    }

    @PutMapping("/{idOrdem}")
    public OrdemServicoResponseDTO atualizar(@PathVariable Long idOrdem, @RequestBody @Valid OrdemServicoRequestDTO dto) {
        return service.atualizarOrdem(idOrdem, dto);
    }
}