package com.manutech.ManuTech.dto;

import com.manutech.ManuTech.model.PerfilUsuario;

public record UsuarioResponseDTO(

        //informações do usuário
        Long idUsuario,
        String login,
        PerfilUsuario perfil,

        //informações do tecnico
        Long idTecnico,
        String nomeTecnico
) {}