package com.manutech.ManuTech.service;

import com.manutech.ManuTech.dto.OrdemServicoResponseDTO;
import com.manutech.ManuTech.model.Maquina;
import com.manutech.ManuTech.model.OrdemServico;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdemServicoService {
    private final OrdemServicoService repository;
    private final MaquinaService maquinaRepository;


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
              ordemServico.getMaquina()
        );
    }
    public OrdemServicoResponseDTO salvar(OrdemServicoRequestDTO dto){
        OrdemServico ordemServico = new OrdemServicoRepository.findById(dto.idOrdemServico())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ordem do serviço não encontrada"));

        OrdemServico ordemServico = new OrdemServico();
        ordemServico.setTitulo(dto.titulo);
        ordemServico.setDescricao(dto.descricao);
        ordemServico.setPrioridade(dto.prioridade);
        ordemServico.setMaquina(Maquina);

        OrdemServico salva = repository.save(ordemServico);
        return toResponseDTO(salva);
    }

    public List<OrdemServicoResponseDTO> listarPorMaquina(Long idMaquina){
        maquinaRepository.findById(idMaquina)
                .orElseThrow(() -> new RecursoNaoEncontradoException("maquina não encontrada"));

        return repository.findByMaquinaIdMaquina(idMaquina)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public OrdemServicoResponseDTO buscarId(Long idOrdemServico){
        OrdemServico ordemServico = repository.findById(idOrdemServico).orElseThrow(()
                -> new RecursoNaoEncontradoException("Ordem serviço não foi encontrada"));
        return toResponseDTO(ordemServico);
    }

    public void deletar(Long idOrdemServico){
        OrdemServico ordemServico = repository.findById(idOrdemServico).orElseThrow(() ->
                new RecursoNaoEncontradoException("Ordem serviço não encontrado"));
        repository.delete(ordemServico);
    }

}
