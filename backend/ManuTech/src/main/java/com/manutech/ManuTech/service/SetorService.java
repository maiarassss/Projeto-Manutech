package com.manutech.ManuTech.service;

import com.manutech.ManuTech.dto.SetorRequestDTO;
import com.manutech.ManuTech.dto.SetorResponseDTO;
import com.manutech.ManuTech.exception.RecursoNaoEncontradoException;
import com.manutech.ManuTech.model.Setor;
import com.manutech.ManuTech.repository.SetorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetorService {

    private final SetorRepository repository;

    public SetorService (SetorRepository repository){
        this.repository = repository;
    }

    //conversão de entidade para dto
    private SetorResponseDTO toResponseDTO(Setor setor){
        return new SetorResponseDTO(
                setor.getIdSetor(),
                setor.getNomeSetor()
        );
    }

    //findById que outras classes possam usar a partir do SetorService (sem acesso direto ao SetorRepository)
    public Setor buscarEntidade (Long idSetor){
        return repository.findById(idSetor)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Setor não encotrado!"));
    }

    //listagem de todos os setores
    public List<SetorResponseDTO> listarSetores(){
        return repository.findAll().stream().map(this::toResponseDTO).toList();
    }


    public List<SetorResponseDTO> buscarSetorPorNome(String nomeSetor){
        return repository.findByNomeSetorContainingIgnoreCase(nomeSetor)
                .stream().map(this::toResponseDTO).toList();
    }

    //salvar setor para cadastro
    public SetorResponseDTO salvarSetor(SetorRequestDTO dto){

        Setor setor = new Setor();

        setor.setNomeSetor(dto.nomeSetor());
        Setor setorSalvo = repository.save(setor);

        return toResponseDTO(setorSalvo);
    }

    //atualiza atualmente apenas o nome do setor
    public SetorResponseDTO atualizarSetor (Long idSetor, SetorRequestDTO dto){

        //primeiro busca o setor informado
        Setor setor = repository.findById(idSetor)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Setor não encotrado!"));

        setor.setNomeSetor(dto.nomeSetor());
        Setor setorAtualizado = repository.save(setor);

        return toResponseDTO(setorAtualizado);
    }

    //método de exclusão não é necessário por persistência de histórico para relatórios
}
