package com.manutech.ManuTech.dto;

import java.util.List;

public record TecnicoResponseDTO(

        //informações sobre o técnico
        Long idTecnico,
        String nome,
        String telefone,

        //lista todos os setores que ele atende
        List<SetorResponseDTO> setoresAtendidos
) {}