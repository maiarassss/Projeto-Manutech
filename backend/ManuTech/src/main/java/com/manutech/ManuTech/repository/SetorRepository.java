package com.manutech.ManuTech.repository;

import com.manutech.ManuTech.model.Setor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SetorRepository extends JpaRepository<Setor, Long> {

    //consulta de setor por nome
    List<Setor> findByNomeSetorContainingIgnoreCase(String nomeSetor);

    boolean existsByNomeSetorIgnoreCase(String nomeSetor);
    //metodo para verificar a existência de registro no banco com nome informado (exclusivo para cadastro)
}