package com.manutech.ManuTech.service;


import com.manutech.ManuTech.dto.SetorRequestDTO;
import com.manutech.ManuTech.dto.SetorResponseDTO;
import com.manutech.ManuTech.exception.RecursoNaoEncontradoException;
import com.manutech.ManuTech.exception.RegraDeNegocioException;
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

    public SetorResponseDTO toResponseDTO(Setor setor){
        return new SetorResponseDTO(
                setor.getIdSetor(),
                setor.getNomeSetor()
        );
    }

    public Setor buscarEntidade (Long idSetor){
        return repository.findById(idSetor)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Setor não encotrado!"));
    }

    public SetorResponseDTO buscarPorId (Long idSetor){

        Setor setor = buscarEntidade(idSetor);
        return toResponseDTO(setor);
    }

    public List<SetorResponseDTO> listarSetores(){
        return repository.findAll().stream().map(this::toResponseDTO).toList();
    }

    public List<SetorResponseDTO> buscarSetorPorNome(String nomeSetor){

        if(!repository.existsByNomeSetorIgnoreCase(nomeSetor)){
            throw new RegraDeNegocioException("O setor informado não existe.");
        }

        return repository.findByNomeSetorContainingIgnoreCase(nomeSetor)
                .stream().map(this::toResponseDTO).toList();
    }

    public SetorResponseDTO salvarSetor(SetorRequestDTO dto){

        Setor setor = new Setor();

        //verifica se já tem algum setor no banco com o nome informado no request
        if(repository.existsByNomeSetorIgnoreCase(dto.nomeSetor())){
            throw new RegraDeNegocioException("O nome informado já está cadastrado.");
        }

        setor.setNomeSetor(dto.nomeSetor());
        Setor setorSalvo = repository.save(setor);

        return toResponseDTO(setorSalvo);
    }

    public SetorResponseDTO atualizarSetor (Long idSetor, SetorRequestDTO dto){

        //primeiro busca o setor informado
        Setor setor = buscarEntidade(idSetor);

        //verifica a existência do nome no banco de dados
        if(repository.existsByNomeSetorIgnoreCase(dto.nomeSetor())){
            throw new RegraDeNegocioException("O nome informado já está cadastrado.");
        }
        setor.setNomeSetor(dto.nomeSetor());
        Setor setorAtualizado = repository.save(setor);

        return toResponseDTO(setorAtualizado);
    }

    //metodo de exclusão não é necessário por persistência de histórico para relatórios
}