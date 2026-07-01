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
    //injeção de dependência do banco
    public SetorService (SetorRepository repository){
        this.repository = repository;
    }

    //converte a entidade crua em dto; recebe as informações da entidade e filtra para o que for ser exibido no response
    public SetorResponseDTO toResponseDTO(Setor setor){
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

    public SetorResponseDTO buscarPorId (Long idSetor){

        Setor setor = buscarEntidade(idSetor);
        //reaproveita a verificação que a busca por entidade faz e retorna em formato dto
        return toResponseDTO(setor);
    }

    //listagem de todos os setores;.map(traz todos os registros, transforma em dto e retorna em forma de lista)
    public List<SetorResponseDTO> listarSetores(){
        return repository.findAll().stream().map(this::toResponseDTO).toList();
    }

    public List<SetorResponseDTO> buscarSetorPorNome(String nomeSetor){

        if(!repository.existsByNomeSetorIgnoreCase(nomeSetor)){ //apenas chamar um metodo boleano considera automat ele como true
            throw new RegraDeNegocioException("O setor informado não existe.");
        }

        return repository.findByNomeSetorContainingIgnoreCase(nomeSetor)
                .stream().map(this::toResponseDTO).toList();
    }

    //salvar setor para cadastro; recebe as informações inseridas no request,
    // insere elas em um objeto Setor(entidade crua) e depois transforma esse objeto em responseDTO
    public SetorResponseDTO salvarSetor(SetorRequestDTO dto){

        Setor setor = new Setor();

        //verifica se já tem algum setor no banco com o nome informado no request
        if(repository.existsByNomeSetorIgnoreCase(dto.nomeSetor())){ //apenas chamar um metodo boleano considera automat ele como true
            throw new RegraDeNegocioException("O nome informado já está cadastrado.");
        }

        setor.setNomeSetor(dto.nomeSetor());
        Setor setorSalvo = repository.save(setor);

        return toResponseDTO(setorSalvo);
    }

    //atualiza atualmente apenas o nome do setor
    public SetorResponseDTO atualizarSetor (Long idSetor, SetorRequestDTO dto){

        //primeiro busca o setor informado
        Setor setor = buscarEntidade(idSetor);

        //para não atualizar com o mesmo nome (porque assim nem precisa atualizar )
        if(repository.existsByNomeSetorIgnoreCase(dto.nomeSetor())){
            throw new RegraDeNegocioException("O nome informado já está cadastrado.");
        }
        setor.setNomeSetor(dto.nomeSetor());
        Setor setorAtualizado = repository.save(setor);

        return toResponseDTO(setorAtualizado);
    }

    //método de exclusão não é necessário por persistência de histórico para relatórios
}