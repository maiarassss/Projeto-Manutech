package com.manutech.ManuTech.dto;


import com.manutech.ManuTech.model.Prioridade;
import com.manutech.ManuTech.model.StatusOrdem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrdemServicoRequestDTO(

        @NotBlank(message = "*Informe um breve título")
        String titulo,

        @NotBlank(message = "*Informe uma breve descrição")
        String descricao,

        @NotNull(message = "*Informe a prioridade")
        Prioridade prioridade,

        @NotNull(message = "*Informe o status")
        StatusOrdem status,

        @NotNull(message = "*Informe a máquina")
        Long idMaquina,

        Long idTecnico
        //nao é obrigatorio porque uma ordem não precisa ser vinculada a um técnico assim qu é aberta
) {}
