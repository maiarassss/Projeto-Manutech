package com.manutech.ManuTech.service;

import com.manutech.ManuTech.dto.OrdemServicoRequestDTO;
import com.manutech.ManuTech.dto.OrdemServicoResponseDTO;
import com.manutech.ManuTech.exception.RecursoNaoEncontradoException;
import com.manutech.ManuTech.model.OrdemServico;
import com.manutech.ManuTech.repository.MaquinaRepository;
import com.manutech.ManuTech.repository.OrdemServicoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdemServicoService {

    private final OrdemServicoRepository repository;
    private final MaquinaRepository maquinaRepository;


     public OrdemServicoService(OrdemServicoRepository repository, MaquinaRepository maquinaRepository){
         this.repository = repository;
     this.maquinaRepository = maquinaRepository;

}

private OrdemServicoResponseDTO toResponseDTO(OrdemServico ordemServico){
    return new OrdemServicoResponseDTO(
            ordemServico.getIdOrdem(),
            ordemServico.getTitulo(),
            ordemServico.getDescricao(),
            ordemServico.getPrioridade(),
            ordemServico.getStatus(),

            ordemServico.getMaquina().getIdMaquina(),
            ordemServico.getMaquina().getCodigoIdentificador(),
            ordemServico.getMaquina().getModelo(),
            ordemServico.getMaquina().getAtiva(),
            ordemServico.getMaquina().getSetor().getIdSetor(),
            ordemServico.getMaquina().getSetor().getNomeSetor()
    );
}
 public OrdemServicoResponseDTO salvar(OrdemServicoRequestDTO dto){


OrdemServico ordemServico = new OrdemServico();

  ordemServico.setTitulo(dto.titulo());
  ordemServico.setDescricao(dto.descricao());
  ordemServico.setPrioridade(dto.prioridade());
  ordemServico.setStatus(dto.status());



   OrdemServico salvar = repository.save(ordemServico);
    return toResponseDTO(salvar);
}

 public List<OrdemServicoResponseDTO> listarTodos(){
  return repository.findAll()
      .stream()
      .map(this::toResponseDTO)
       .toList();
 }

  public OrdemServicoResponseDTO buscarId(Long idOrdemServico){
   OrdemServico ordemServico = repository.findById(idOrdemServico).orElseThrow(()
           -> new RecursoNaoEncontradoException("Ordem serviço não foi encontrada"));
   return toResponseDTO(ordemServico);
}

 public void deletar(Long idOrdemServico) {
   OrdemServico ordemServico = repository.findById(idOrdemServico)
           .orElseThrow(() ->
           new RecursoNaoEncontradoException("Ordem serviço não encontrado"));

   repository.delete(ordemServico);
}

    public OrdemServicoResponseDTO atualizar(Long idOrdemService, OrdemServicoRequestDTO dto) {
        OrdemServico existente = repository.findById(idOrdemService).orElseThrow(() ->
              new RecursoNaoEncontradoException("Ordem de serviço não Encontrada"));
        existente.setTitulo(dto.titulo());
        existente.setDescricao(dto.descricao());
        existente.setPrioridade(dto.prioridade());
        existente.setStatus(dto.status());

        OrdemServico atulizado = repository.save(existente);
        return toResponseDTO(atulizado);
    }
}



