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
@Table(name = "TB_ORDEMSERVICO")
public class OrdemServico { //dependente da máquina

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOrdem;

    private String titulo;
    private String descricao;

    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;
    //enuns
    @Enumerated(EnumType.STRING)
    private StatusOrdem status;


    @ManyToOne //muitas demandas diferentes podem ser feitas para a mesma máquina
    @JoinColumn(name = "FK_idMaquina")
    private Maquina maquina;
    //RN28: toda ordem deve estar obrigatoriamente vinculada em alguma máquina
}
