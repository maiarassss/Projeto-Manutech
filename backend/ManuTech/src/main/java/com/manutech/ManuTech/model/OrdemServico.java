package com.manutech.ManuTech.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "TBORDEMSERVICO")
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOrdem;

    private String titulo;
    private String descricao;
    private Prioridade prioridade;

    @OneToOne
    @JoinColumn(name = "idMaquina")
    private Maquina maquina;
}
