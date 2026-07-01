package com.manutech.ManuTech.dto;

import com.manutech.ManuTech.model.PerfilUsuario;

public record LoginResponseDTO(
        Long idUsuario,
        String login,
        PerfilUsuario perfil,
        String mensagem
) {}
