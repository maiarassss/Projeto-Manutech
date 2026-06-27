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
public class Setor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSetor;

    //configurações de banco sempre na entidade
    @Column(unique = true, nullable = false, length = 100) //*rn para que não haja mais setores com o mesmo nome
    private String nomeSetor;

    @OneToMany(mappedBy = "setor")
    private List<Maquina> listaMaquinas = new ArrayList<>();
    //um setor pode armazenar suas máquinas dependentes

    //mappedBy para manter o relacionamento bidirecional
    @ManyToMany(mappedBy = "setoresAtendidos")
    private List<Tecnico> listaTecnicos = new ArrayList<>();
    //um setor pode ver todos os seus técnicos
}
