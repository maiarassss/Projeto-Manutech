package com.manutech.ManuTech.dto;

import com.manutech.ManuTech.model.Prioridade;

public record OrdemServicoResponseDTO (
        Long id,
        String titulo,
        String descricao,
        Prioridade prioridade
) {
}
