package com.manutech.ManuTech.repository;

import com.manutech.ManuTech.model.Tecnico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TecnicoRepository extends JpaRepository<Tecnico, Long> {

    List<Tecnico> findByNomeContainingIgnoreCase(String nome);

    boolean existsByNomeIgnoreCase(String nome);

    //busca os técnicos de determinado setor
    List<Tecnico> findBySetoresAtendidosIdSetor(Long idSetor);
}