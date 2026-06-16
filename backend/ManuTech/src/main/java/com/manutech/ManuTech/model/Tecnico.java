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
@Table(name = "TBTECNICO")
public class Tecnico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTecnico;

    private String nome;
    private String telefone;


    //aqui é gerada uma tabela intermediária para relacionar Tecnico e Setor
    @ManyToMany
    @JoinTable(name = "TB_TECNICO_SETOR", //nome da tabela extra no banco
               joinColumns = @JoinColumn(name = "FK_idTecnico"), // fk que vem do Tecnico
               inverseJoinColumns = @JoinColumn(name = "FK_idSetor"))// fk que vem do Setor
    //anotação apenas para formatação de nomenclatura no banco de dados

    private List<Setor> setoresAtendidos = new ArrayList<>();
    //um técnico pode atender mais de um setor, tanto para cadastro quanto consulta eles ficam em uma lista
}
