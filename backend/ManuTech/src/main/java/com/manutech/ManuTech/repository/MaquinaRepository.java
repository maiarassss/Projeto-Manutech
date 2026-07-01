package com.manutech.ManuTech.repository;

import com.manutech.ManuTech.model.Maquina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MaquinaRepository extends JpaRepository<Maquina, Long> {

    List<Maquina> findByCodigoIdentificadorContainingIgnoreCase(String codigoIdentificador);

    List<Maquina> findByModeloContainingIgnoreCase(String modelo);

    List<Maquina> findByAtiva(Boolean ativa);

    List<Maquina> findBySetorIdSetor(Long idSetor);

    List<Maquina> findByModeloAndSetorIdSetor(String modelo, Long idSetor);

    boolean existsByCodigoIdentificadorIgnoreCase(String codigo);
    //verifica se o código identificador já existe
}