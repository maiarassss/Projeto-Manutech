package com.manutech.ManuTech.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TBUSUARIO")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PerfilUsuario perfil; //gestor ou tecnico

    //atribui um tecnico ao usuario apenas se o perfil for tecnico
    @OneToOne
    @JoinColumn(name = "id_tecnico", unique = true) //um idTecnico so pode ser vinculado a um login
    private Tecnico tecnico;
}