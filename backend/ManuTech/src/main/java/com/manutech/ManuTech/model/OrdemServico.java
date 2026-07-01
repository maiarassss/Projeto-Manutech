package com.manutech.ManuTech.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "TBORDEMSERVICO")
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOrdem;

    private String titulo;
    private String descricao;

    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;
    @Enumerated(EnumType.STRING)
    private StatusOrdem status;

    @ManyToOne
    @JoinColumn(name = "FK_idMaquina")
    private Maquina maquina;
    //para uma ordem ser registrada precisa informar a máquina com defeito

    @ManyToOne
    @JoinColumn(name = "id_tecnico")
    private Tecnico tecnico;
    //relacionamento unidirecional: apenas a ordem conhece o tecnico que a atendeu
}