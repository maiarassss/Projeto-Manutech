package com.manutech.ManuTech.service;

import com.manutech.ManuTech.dto.TecnicoRequestDTO;
import com.manutech.ManuTech.dto.TecnicoResponseDTO;
import com.manutech.ManuTech.exception.RecursoNaoEncontradoException;
import com.manutech.ManuTech.model.Setor;
import com.manutech.ManuTech.model.Tecnico;
import com.manutech.ManuTech.repository.TecnicoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TecnicoService {

    private final TecnicoRepository repository;

    //injeção de dependência do Setor para que o técnico possa ser atrelado a um setor (ou mais)
    private final SetorService setorService;

    public TecnicoService(TecnicoRepository repository, SetorService setorService) {
        this.repository = repository;
        this.setorService = setorService;
    }

    private TecnicoResponseDTO toResponseDTO(Tecnico tecnico) {
        return new TecnicoResponseDTO(
                //apenas get para trazer atributos únicos
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

    //lista técnicos por nome
    public List<TecnicoResponseDTO> buscarPorNome(String nome){
        return repository.findByNomeContainingIgnoreCase(nome)
                .stream().map(this::toResponseDTO).toList();
                // cada item listado é convertido em dto s
    }

    //lista tdos os técnicos relacionados ao setor informado
    public List<TecnicoResponseDTO> listarTecnicosPorSetor(Long idSetor){
        return repository.findBySetoresAtendidosIdSetor(idSetor)
                .stream().map(this::toResponseDTO).toList();
    }


    public List<TecnicoResponseDTO> listarTecnicos() {
        return repository.findAll().stream().map(this::toResponseDTO).toList();
    }


    //metodo que vai percorrer a lista de id setores(do tecnico) e para cada id usa o metodo de buscar entidade para
    //retornar o setor correspondente e guardar ele(s) em uma outra lista
    public List<Setor> buscarSetores(List<Long> idsSetores) {
        return idsSetores
                .stream()
                .map(setorService::buscarEntidade)
                .toList();
    }


    public TecnicoResponseDTO salvarTecnico(TecnicoRequestDTO dto) {
        //passa os dados do request para um objeto vazio Tecnico e o transforma em dto
        Tecnico tecnico = new Tecnico();

        tecnico.setNome(dto.nome());
        tecnico.setTelefone(dto.telefone());

        //conflito porque os tipos das listas são diferentes
        // tecnico.setSetoresAtendidos(dto.idsSetores());

        //busca os ids em dto através dometodo de buscarEntidade e guarda cada objeto puxado dentro de uma lista
        // do tipo Setor(que possa ser usada como parâmetro para a modificação da lista de setores original da entidade)


        //usa o metodo que puxa o id de cada setor (ainda que um id de dto) para modificar a lista de setoresAtendidos
        //para a lista de ids 'puxados' do dto
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
