package com.manutech.ManuTech.repository;

import com.manutech.ManuTech.model.Maquina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaquinaRepository extends JpaRepository<Maquina, Long> {

    //porque o técnico ou gestor dificilmente vão saber o id que está no banco
    List<Maquina> findByCodigoIdentificador(String codigoIdentificador);

    List<Maquina> findByModeloContainingIgnoreCase(String modelo);

    List<Maquina> findByAtiva(Boolean ativa);

    List<Maquina> findBySetorIdSetor(Long idSetor);

    //para listar máquinas de determinado modelo e setor
    List<Maquina> findByModeloAndSetorIdSetor(String modelo, Long idSetor);
}
