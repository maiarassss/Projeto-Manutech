package com.manutech.ManuTech.dto;

import com.manutech.ManuTech.model.PerfilUsuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioRequestDTO(

        @NotBlank(message = "*Informe o login de acesso")
        String login,

        @NotBlank(message = "*Informe a senha")
        String senha,

        @NotNull(message = "*Informe o perfil")
        PerfilUsuario perfil,

        //só é obrigatorio colocar se o perfil for tecnico
        Long idTecnico
) {}