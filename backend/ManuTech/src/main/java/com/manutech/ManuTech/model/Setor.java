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
@Table(name = "TBSETOR")
public class Setor { //entidade principal

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSetor;

    private String nomeSetor;


    //relação bidirecional para que o setor possa acessar seus tecnicos
    @ManyToMany(mappedBy = "setoresAtendidos")
    private List<Tecnico> listaTecnicos = new ArrayList<>();


    //um setor contém muitas máquinas
    @OneToMany(mappedBy = "setor")
    private List<Maquina> listaMaquinas = new ArrayList<>();
    //o setor pode acessar os dados de cada máquina dependente para gerar os relatórios de desempenho do setpr
}
