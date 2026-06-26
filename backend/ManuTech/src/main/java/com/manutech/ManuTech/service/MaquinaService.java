package com.manutech.ManuTech.service;

import com.manutech.ManuTech.dto.MaquinaResponseDTO;
import com.manutech.ManuTech.exception.RecursoNaoEncontradoException;
import com.manutech.ManuTech.model.Maquina;
import com.manutech.ManuTech.repository.MaquinaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaquinaService {

    private final MaquinaRepository repository;
//    private final OrdemServicoService ordemService;

    public MaquinaService(MaquinaRepository repository){// OrdemServicoService ordemService){
        this.repository = repository;
//        this.ordemService = ordemService;
        //apesar da máquina em si ser independente da ordem, o histórico dela não é
    }

    public Maquina buscarEntidade(Long idMaquina){
        return repository.findById(idMaquina)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Máquina não encontrada!"));
    }

    public MaquinaResponseDTO toResponseDTO(Maquina maquina){
        return new MaquinaResponseDTO(
                maquina.getIdMaquina(),
                maquina.getCodigoIdentificador(),
                maquina.getModelo(),
                maquina.getAtiva(),
                maquina.getSetor().getIdSetor(),
                maquina.getSetor().getNomeSetor()
        );
    }

    public MaquinaResponseDTO buscarPorId(Long idMaquina){
        Maquina maquina = buscarEntidade(idMaquina);
        return toResponseDTO(maquina);
    }

    public MaquinaResponseDTO buscarPorCodigo(String codigoIdentificador){
        Maquina maquina = repository.findByCodigoIdentificador(codigoIdentificador)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Máquina não encontrada!"));

        return toResponseDTO(maquina);
    }

    public List<MaquinaResponseDTO> buscarPorModelo(String modelo){
        return repository.findByModeloContainingIgnoreCase(modelo)
                .stream()
                .map(this::toResponseDTO)
                .toList();
        //retorna uma lista com os itens já convertidos em dto
    }

    //lista todas as máquinas cadastradas no sistema
    public List<MaquinaResponseDTO> listarMaquinas(){
        return repository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<MaquinaResponseDTO> listarMaquinasPorAtividade(Boolean ativa){
        return repository.findByAtiva(ativa)
                .stream()
                .map(this::toResponseDTO)
                .toList();

    }

    //lista as máquinas atráves do setor indicado
    public List<MaquinaResponseDTO> listarMaquinasPorSetor(Long idSetor){
        return repository.findBySetorIdSetor(idSetor)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }
}
