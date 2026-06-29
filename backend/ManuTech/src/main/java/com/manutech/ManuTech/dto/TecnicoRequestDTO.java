package com.manutech.ManuTech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TecnicoRequestDTO(

        @NotBlank(message = "*Informe o nome do técnico")
        String nome,

        @NotBlank(message = "*Informe o telefone do técnico")
        String telefone,

        //uma lista para registrar mais de um setor por técnico, que não pode estar vazia
        @NotEmpty(message = "*Informe o(s) setor(s) do técnico")
        List<Long> idsSetores
) {}