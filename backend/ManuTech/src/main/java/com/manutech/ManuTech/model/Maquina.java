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

//    @OneToMany
//    @JoinColumn(name = "idOrdem")
//    private List<OrdemServico> ordem = new ArrayList<>();
//    //lista de ordens que a máquina tem registrada
}
