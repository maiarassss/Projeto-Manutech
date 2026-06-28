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

    //RN24: código obrigatório para cadastrar máquina que seja unico
    @Column(unique = true, nullable = false, length = 100)
    private String codigoIdentificador;

    private String modelo;

    //informa o estado atual de atividade da máquina
    private Boolean ativa;

    //muitas máquinas podem pertencer a um setor
    @ManyToOne
    @JoinColumn(name = "FK_idSetor")
    private Setor setor;

    //uma maquina pode armazenar todas as suas demandas
    @OneToMany(mappedBy = "maquina")
    private List<OrdemServico> listaOrdens = new ArrayList<>();
}
