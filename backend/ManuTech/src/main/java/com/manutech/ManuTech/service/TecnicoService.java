package com.manutech.ManuTech.service;

import com.manutech.ManuTech.dto.TecnicoRequestDTO;
import com.manutech.ManuTech.dto.TecnicoResponseDTO;
import com.manutech.ManuTech.exception.RecursoNaoEncontradoException;
import com.manutech.ManuTech.exception.RegraDeNegocioException;
import com.manutech.ManuTech.model.Setor;
import com.manutech.ManuTech.model.Tecnico;
import com.manutech.ManuTech.repository.TecnicoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TecnicoService {

    private final TecnicoRepository repository;
    private final SetorService setorService;

    public TecnicoService(TecnicoRepository repository, SetorService setorService) {
        this.repository = repository;
        this.setorService = setorService;
    }

    private TecnicoResponseDTO toResponseDTO(Tecnico tecnico) {
        return new TecnicoResponseDTO(
                tecnico.getIdTecnico(),
                tecnico.getNome(),
                tecnico.getTelefone(),

                tecnico.getSetoresAtendidos() //traz a lista de setores
                        .stream()
                        //cada setor da lista vai ser transformado em dto tambem (pelo metodo de conversao do próprio setor service)
                        .map(setorService::toResponseDTO)
                        .toList()
        );
    }

    public Tecnico buscarEntidade(Long idTecnico) {
        return repository.findById(idTecnico)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Técnico não encontrado!"));
    }

    public TecnicoResponseDTO buscarPorId(Long idTecnico) {

        Tecnico tecnico = buscarEntidade(idTecnico);
        return toResponseDTO(tecnico);
    }

    public List<TecnicoResponseDTO> buscarPorNome(String nome){

        if(!repository.existsByNomeIgnoreCase(nome)){
            throw new RegraDeNegocioException("O técnico informado não está cadastrado.");
        }

        return repository.findByNomeContainingIgnoreCase(nome)
                .stream().map(this::toResponseDTO).toList();
    }

    //lista tdos os técnicos relacionados ao setor informado
    public List<TecnicoResponseDTO> listarTecnicosPorSetor(Long idSetor){
        Setor setor = setorService.buscarEntidade(idSetor);

        return repository.findBySetoresAtendidosIdSetor(idSetor)
                .stream().map(this::toResponseDTO).toList();
    }

    public List<TecnicoResponseDTO> listarTecnicos() {
        return repository.findAll().stream().map(this::toResponseDTO).toList();
    }

    //percorre a lista de idSetor que o técnico atende e para cada id usa o metodo de buscar entidade (do SetorService)
    public List<Setor> buscarSetores(List<Long> idsSetores) {
        return new ArrayList<>( //arraylist para que não retorne uma lista imutável
                idsSetores.stream()
                        .map(setorService::buscarEntidade)
                        .toList()
                //cada setor retornado é armazenado em outra lista
        );
    }

    public TecnicoResponseDTO salvarTecnico(TecnicoRequestDTO dto) {

        Tecnico tecnico = new Tecnico();

        tecnico.setNome(dto.nome());
        tecnico.setTelefone(dto.telefone());

        //busca os idsSetores em dto e guarda o que é retornado dentro de uma lista
        //do tipo Setor(que possa ser usada como parâmetro para a modificação da lista de setores original da entidade)
        tecnico.setSetoresAtendidos(buscarSetores(dto.idsSetores()));

        Tecnico tecnicoSalvo = repository.save(tecnico);

        return toResponseDTO(tecnicoSalvo);
    }


    public TecnicoResponseDTO atualizarTecnico(Long idTecnico, TecnicoRequestDTO dto) {

        //primeiro busca o técnico no banco
        Tecnico tecnico = buscarEntidade(idTecnico);

        //modifica os dados antigos para os atuais passados no request
        tecnico.setNome(dto.nome());
        tecnico.setTelefone(dto.telefone());

        //lista atual de setores <- puxa entidade pelo id <- lista de id do dto
        tecnico.setSetoresAtendidos(buscarSetores(dto.idsSetores()));

        Tecnico tecnicoSalvo = repository.save(tecnico);

        return toResponseDTO(tecnicoSalvo);
    }

    //sem exclusão de técnico para persistência de histórico
}