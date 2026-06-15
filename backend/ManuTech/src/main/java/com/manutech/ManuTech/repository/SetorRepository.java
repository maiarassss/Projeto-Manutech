package com.manutech.ManuTech.repository;

import com.manutech.ManuTech.model.Setor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SetorRepository extends JpaRepository<Setor, Long> {

    List<Setor> findByNomeSetorContainingIgnoreCase(String nomeSetor);
}
