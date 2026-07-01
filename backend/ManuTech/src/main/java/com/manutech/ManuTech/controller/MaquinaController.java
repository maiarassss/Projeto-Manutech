package com.manutech.ManuTech.controller;

import com.manutech.ManuTech.dto.MaquinaRequestDTO;
import com.manutech.ManuTech.dto.MaquinaResponseDTO;
import com.manutech.ManuTech.dto.OrdemServicoResponseDTO;
import com.manutech.ManuTech.service.MaquinaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/maquinas")
public class MaquinaController {

    private final MaquinaService service;

    public MaquinaController(MaquinaService service) {
        this.service = service;
    }

    @GetMapping
    public List<MaquinaResponseDTO> listar(){
        return service.listarMaquinas();
    }

    @GetMapping("/atividade")
    public List<MaquinaResponseDTO> listarPorAtividade(@RequestParam Boolean ativa){
        return service.listarMaquinasPorAtividade(ativa);
    }

    @GetMapping("/setor")
    public List<MaquinaResponseDTO> listarPorSetor(@RequestParam Long idSetor){
        return service.buscarMaquinasPorSetor(idSetor);
    }

    @GetMapping("/ordens")
    public List<OrdemServicoResponseDTO> listarOrdens(@RequestParam String codigoIdentificador){
        return service.listarOrdens(codigoIdentificador);
    }

    @GetMapping("/{idMaquina}")
    public MaquinaResponseDTO buscarPorId(@PathVariable Long idMaquina){
        return service.buscarPorId(idMaquina);
    }

    @GetMapping("/codigo")
    public List<MaquinaResponseDTO> buscarPorCodigo(@RequestParam String codigoIdentificador){
        return service.buscarPorCodigo(codigoIdentificador);
    }

    @GetMapping("/modelo")
    public List<MaquinaResponseDTO> buscarPorModelo(@RequestParam String modelo){
        return service.buscarPorModelo(modelo);
    }

    @GetMapping("/modelo/setor")
    public List<MaquinaResponseDTO> buscarPorModeloESetor(@RequestParam String modelo, @RequestParam Long idSetor){
        return service.buscarPorModeloESetor(modelo, idSetor);
    }

    @PostMapping
    public MaquinaResponseDTO salvar (@RequestBody @Valid MaquinaRequestDTO dto){
        return service.salvarMaquina(dto);
    }

    @PutMapping("/{idMaquina}")
    public MaquinaResponseDTO atualizar (@PathVariable Long idMaquina, @RequestBody @Valid MaquinaRequestDTO dto){
        return service.atualizarMaquina(idMaquina, dto);
    }

    @DeleteMapping("/{idMaquina}")
    public void deletar(@PathVariable Long idMaquina){
        service.deletarMaquina(idMaquina);
    }
}