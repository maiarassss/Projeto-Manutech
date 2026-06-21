package com.manutech.ManuTech.dto;

import jakarta.validation.constraints.NotBlank;

public record SetorRequestDTO(

        @NotBlank(message = "*Informe o nome do setor")
        String nomeSetor
) {}