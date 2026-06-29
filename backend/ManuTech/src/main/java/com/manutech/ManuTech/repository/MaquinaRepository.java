package com.manutech.ManuTech.repository;

import com.manutech.ManuTech.model.Maquina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MaquinaRepository extends JpaRepository<Maquina, Long> {

    //retorna uma lista para conseguir usar o containing
    List<Maquina> findByCodigoIdentificadorContainingIgnoreCase(String codigoIdentificador);

    List<Maquina> findByModeloContainingIgnoreCase(String modelo);

    List<Maquina> findByAtiva(Boolean ativa);

    List<Maquina> findBySetorIdSetor(Long idSetor);

    //para listar máquinas de determinado modelo e setor; ajuste na sintaxe
    List<Maquina> findByModeloAndSetorIdSetor(String modelo, Long idSetor);

    boolean existsByCodigoIdentificadorIgnoreCase(String codigo);
    //apenas para verificar se o mesmo codigo pretende ser cadastrado novamente

}
