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
public class Maquina { //dependente do setor

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMaquina;

    //RN24: código obrigatório para cadastrar máquina, também para caso de máquinas do mesmo modelo
    private String codigoIdentificador;

    private String modelo;
    private Boolean ativa;

    @ManyToOne //muitas máquinas podem ser usadas em um único setor
    @JoinColumn(name = "FK_idSetor")
    private Setor setor;

    @OneToMany(mappedBy = "maquina")
    private List<OrdemServico> listaOrdens = new ArrayList<>();
    //cada máquina pode registrar suas demandas de manutenção para fins de análise de desempenho
}
