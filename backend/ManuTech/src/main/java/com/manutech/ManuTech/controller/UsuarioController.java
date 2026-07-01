package com.manutech.ManuTech.controller;

import com.manutech.ManuTech.dto.UsuarioRequestDTO;
import com.manutech.ManuTech.dto.UsuarioResponseDTO;
import com.manutech.ManuTech.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    public UsuarioResponseDTO salvar(@RequestBody @Valid UsuarioRequestDTO dto) {
        return service.salvarUsuario(dto);
    }

    @DeleteMapping("/{idUsuario}")
    public void deletar(@PathVariable Long idUsuario) {
        service.deletarUsuario(idUsuario);
    }
}