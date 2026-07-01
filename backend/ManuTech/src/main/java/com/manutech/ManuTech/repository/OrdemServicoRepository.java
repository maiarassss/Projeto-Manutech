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

    List<OrdemServico> findByMaquinaCodigoIdentificadorContainingIgnoreCase(String codigoIdentificador);

    //consulta de ordens pelo nome do setor que a máquina pertence
    List<OrdemServico> findByMaquinaSetorNomeSetorContainingIgnoreCase(String nomeSetor);

    //consulta todas as ordens atendidas por determindado técnico
    List<OrdemServico> findByTecnicoIdTecnico(Long idTecnico);
}