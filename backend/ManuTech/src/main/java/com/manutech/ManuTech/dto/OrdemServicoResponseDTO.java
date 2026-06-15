package com.manutech.ManuTech.dto;

import com.manutech.ManuTech.model.Prioridade;
import com.manutech.ManuTech.model.StatusOrdem;

public record OrdemServicoResponseDTO(

        //informações sobre a ordem
        Long idOrdem,
        String titulo,
        String descricao,
        Prioridade prioridade,
        StatusOrdem status,

        //informações sobre a máquina que a ordem solicita
        Long idMaquina,
        String codigoIdentificador,
        String modelo,
        Boolean ativa,
        Long idSetor,
        String nomeSetor
) {}