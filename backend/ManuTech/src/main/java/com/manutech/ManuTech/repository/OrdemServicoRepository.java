package com.manutech.ManuTech.repository;


import com.manutech.ManuTech.model.OrdemServico;
import com.manutech.ManuTech.model.Prioridade;
import com.manutech.ManuTech.model.StatusOrdem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {

    List<OrdemServico> findByTituloContainingIgnoreCase(String titulo);

    List<OrdemServico> findByPrioridade(Prioridade prioridade);

    List<OrdemServico> findByStatus(StatusOrdem status);

    //alterar id para codigo para que o tecnico possa pesquisar as ordens, ele não tem acesso ao id
    List<OrdemServico> findByMaquinaCodigoIdentificador(String codigoIdentificador);

    //consulta de ordens pelo nome do setor da máquina
    List<OrdemServico> findByMaquinaSetorNomeSetorContainingIgnoreCase(String nomeSetor);
}
