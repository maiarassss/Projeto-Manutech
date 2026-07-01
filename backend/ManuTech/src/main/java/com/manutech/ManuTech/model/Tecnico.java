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

    @ManyToMany //sem definir coluna para FK uma tabela intermediária é criada
    @JoinTable(name = "TB_TECNICO_SETOR", //formatação de nomenclatura no banco
            joinColumns = @JoinColumn(name = "FK_idTecnico"),
            inverseJoinColumns = @JoinColumn(name = "FK_idSetor"))
    private List<Setor> setoresAtendidos = new ArrayList<>();
}