package com.manutech.ManuTech.controller;

import com.manutech.ManuTech.dto.TecnicoRequestDTO;
import com.manutech.ManuTech.dto.TecnicoResponseDTO;
import com.manutech.ManuTech.service.TecnicoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tecnicos")
public class TecnicoController {

    private final TecnicoService service;

    public TecnicoController(TecnicoService service) {
        this.service = service;
    }

    @GetMapping
    public List<TecnicoResponseDTO> listar(){
        return service.listarTecnicos();
    }

    //rota exclusiva, apenas {idSetor} poderia ser confundido com a busca de tecnico por id
    @GetMapping("/setor/{idSetor}")
    public List<TecnicoResponseDTO> listarPorSetor(@PathVariable Long idSetor){
        return service.listarTecnicosPorSetor(idSetor);
    }

    @GetMapping("/{idTecnico}")
    public TecnicoResponseDTO buscarPorId(@PathVariable Long idTecnico){
        return service.buscarPorId(idTecnico);
    }

    @GetMapping("/nome")
    public List<TecnicoResponseDTO> buscarPorNome(@RequestParam String nomeTecnico){
        return service.buscarPorNome(nomeTecnico);
    }

    @PostMapping
    public TecnicoResponseDTO salvar(@RequestBody @Valid TecnicoRequestDTO dto){
        return service.salvarTecnico(dto);
    }

    @PutMapping("/{idTecnico}")
    public TecnicoResponseDTO atualizar(@PathVariable Long idTecnico, @RequestBody @Valid TecnicoRequestDTO dto){
        return service.atualizarTecnico(idTecnico, dto);
    }
}