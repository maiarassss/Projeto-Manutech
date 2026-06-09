package com.manutech.ManuTech.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "TBMAQUINA")
public class Maquina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMaquina;

    private String modelo;
    private Boolean ativa;

    @OneToOne
    @JoinColumn(name = "idOrdem")
    private List<OrdemServico> ordem = new ArrayList<>();
    //lista de ordens que a máquina tem registrada
}
