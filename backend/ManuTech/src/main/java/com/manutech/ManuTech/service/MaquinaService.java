package com.manutech.ManuTech.service;

import com.manutech.ManuTech.dto.MaquinaRequestDTO;
import com.manutech.ManuTech.dto.MaquinaResponseDTO;
import com.manutech.ManuTech.exception.RecursoNaoEncontradoException;
import com.manutech.ManuTech.exception.RegraDeNegocioException;
import com.manutech.ManuTech.model.Maquina;
import com.manutech.ManuTech.repository.MaquinaRepository;

import java.util.List;

public class MaquinaService {
    private final MaquinaRepository repository;

    public MaquinaService(MaquinaRepository repository){
        this.repository = repository;
    }

    //Atributos "códigoIdentificador" e "idMaquina" são gerados automaticamente
    public MaquinaResponseDTO salvar(MaquinaRequestDTO dto){
        Maquina maquina = new Maquina();
        maquina.setModelo(dto.modelo());
        maquina.setAtiva(dto.ativa());

        Maquina salvo = repository.save(maquina);
        return toResponseDTO(salvo);
    }

    private MaquinaResponseDTO toResponseDTO(Maquina maquina){
        return new MaquinaResponseDTO(
                maquina.getIdMaquina(),
                maquina.getModelo()
        );
    }

    public List<MaquinaResponseDTO> listar(){
        return repository.findAll().stream().map(this::toResponseDTO).toList();
    }

    public MaquinaResponseDTO buscarPorId(Long idMaquina){
        Maquina maquina = repository.findById(idMaquina)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Máquina não encontrada"));
        return toResponseDTO(maquina);
    }

    public MaquinaResponseDTO atualizar(Long idMaquina, MaquinaRequestDTO dto){
        Maquina maquina =  repository.findById(idMaquina)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Maquina não encontrada"));

        diretor.setNome(dto.nome());
        diretor.setNacionalidade(dto.nacionalidade());
        diretor.setIdade(dto.idade());
        diretor.setBiografia(dto.biografia());

        Diretor atualizado = repository.save(diretor);

        return toResponseDTO(atualizado);
    }

    //se o diretor tem filmes no nome dele, não pode ser deletado

    public void deletar(Long idDiretor){
        Diretor diretor = repository.findById(idDiretor)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Diretor não encontrado"));

        if (!diretor.getFilmes().isEmpty()){
            throw new RegraDeNegocioException("Não é possível excluir um diretor que possua filmes cadastrados");
        }

        repository.delete(diretor);
    }
}
