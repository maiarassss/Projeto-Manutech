package com.manutech.ManuTech.repository;

import com.manutech.ManuTech.model.Maquina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MaquinaRepository extends JpaRepository<Maquina, Long> {

    //ao invés de retornar uma lista, retorna um objeto ou um erro caso não encontre
    Optional<Maquina> findByCodigoIdentificador(String codigoIdentificador);

    List<Maquina> findByModeloContainingIgnoreCase(String modelo);

    List<Maquina> findByAtiva(Boolean ativa);

    List<Maquina> findBySetorIdSetor(Long idSetor);

    //para listar máquinas de determinado modelo e setor; ajuste na sintaxe
    List<Maquina> findByModeloAndSetorIdSetor(String modelo, Long idSetor);

    //não acho que faça sentido um findByOrdemId porque se eu sei qual é o id da ordem eu sei também qual é a ordem e qual máquina ela atende

}
