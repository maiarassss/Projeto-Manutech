package com.manutech.ManuTech.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MaquinaRequestDTO(

        @NotBlank(message = "*Informe o código identificador da máquina")
        String codigoIdentificador,

        @NotBlank(message = "*Informe o modelo da máquina")
        String modelo,

        @NotNull(message = "*Informe o estado de atividade da máquina")
        Boolean ativa,

        @NotNull(message = "*Informe o setor da máquina")
        Long idSetor
) {}