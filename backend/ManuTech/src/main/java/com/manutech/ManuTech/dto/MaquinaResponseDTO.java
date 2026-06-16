package com.manutech.ManuTech.dto;

public record MaquinaResponseDTO(

        //informações sobre a máquina
        Long idMaquina,
        String codigoIdentificador,
        String modelo,
        Boolean ativa,

        //informações sobre o setor que ela pertence
        Long idSetor,
        String nomeSetor
) {}