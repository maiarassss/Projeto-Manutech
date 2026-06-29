package com.manutech.ManuTech.controller;

import com.manutech.ManuTech.dto.SetorRequestDTO;
import com.manutech.ManuTech.dto.SetorResponseDTO;
import com.manutech.ManuTech.service.SetorService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/setores")
public class SetorController {

    private final SetorService service;

    public SetorController(SetorService service) {
        this.service = service;
    }

    @GetMapping
    public List<SetorResponseDTO> listar(){
        return service.listarSetores();
    }

    @GetMapping("/{idSetor}") //dentro do caminho principal dos setores, vai retornar o correspondente ao id informado
    public SetorResponseDTO buscarPorId(@PathVariable Long idSetor){ //conecta o valor variante do endpoint ao parâmetro necessário para o método
        return service.buscarPorId(idSetor);
    }

    @GetMapping("/nome") //depois desse endpoint haverá um critério de busca com um nome específico
    public List<SetorResponseDTO> buscarPorNome(@RequestParam String nomeSetor){
        return service.buscarSetorPorNome(nomeSetor);
    }

    @PostMapping
    public SetorResponseDTO salvar(@RequestBody @Valid SetorRequestDTO dto){ //json -> java e validação do que está restringido no dto
        return service.salvarSetor(dto);
    }

    @PutMapping("/{idSetor}") //recebe o id do setor a ser alterado, conecta isso ao parametro do método de atualização
    public SetorResponseDTO atualizar(@PathVariable Long idSetor, @RequestBody @Valid SetorRequestDTO dto){
        return service.atualizarSetor(idSetor, dto);
    }
}
