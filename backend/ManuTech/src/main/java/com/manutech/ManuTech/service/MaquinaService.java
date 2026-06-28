package com.manutech.ManuTech.service;

import com.manutech.ManuTech.dto.MaquinaRequestDTO;
import com.manutech.ManuTech.dto.MaquinaResponseDTO;
import com.manutech.ManuTech.dto.OrdemServicoResponseDTO;
import com.manutech.ManuTech.exception.RecursoNaoEncontradoException;
import com.manutech.ManuTech.exception.RegraDeNegocioException;
import com.manutech.ManuTech.model.Maquina;
import com.manutech.ManuTech.model.Setor;
import com.manutech.ManuTech.repository.MaquinaRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaquinaService {

    private final MaquinaRepository repository;
    private final OrdemServicoService ordemService;
    private final SetorService setorService;

    public MaquinaService(MaquinaRepository repository, @Lazy OrdemServicoService ordemService, SetorService setorService){
        this.repository = repository;
        this.ordemService = ordemService;
        //apesar da máquina em si ser independente da ordem, o histórico dela não é

        this.setorService = setorService;
        //setor necessário para registrar e atualizar máquinas
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

    //metodo para que o estado de atividade da maquina possa ser salvo conforme o status da ordem de servico
    public void salvarEntidade(Maquina maquina){
        repository.save(maquina);
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

    //**manter id setor para testar a selecao no front
    public List<MaquinaResponseDTO> buscarPorModeloESetor(String modelo, Long idSetor){

        return repository.findByModeloAndSetorIdSetor(modelo, idSetor)
                .stream()
                .map(this::toResponseDTO)
                .toList();
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

    public List<OrdemServicoResponseDTO> listarOrdens(String codigoIdentificador){
        return ordemService.listarOrdensPorMaquina(codigoIdentificador);
        //não precisa do .map porque dentro do método já tem
    }


    public MaquinaResponseDTO salvarMaquina(MaquinaRequestDTO dto){

        Maquina maquina = new Maquina();

        //**rn cod identificador unico
        //verifica se o código identificador inserido no request já existe no banco de dados
        if (repository.findByCodigoIdentificador(dto.codigoIdentificador()).isPresent()) {
            throw new RegraDeNegocioException("Código identificador já registrado.");
        }
        maquina.setCodigoIdentificador(dto.codigoIdentificador());

        maquina.setModelo(dto.modelo());
        maquina.setAtiva(dto.ativa());

        //primeiro busca no banco o id do setor informado no request
        Setor setor = setorService.buscarEntidade(dto.idSetor());
        maquina.setSetor(setor); //se existir traz esse mesmo setor(entidade) para dentro da entidade maquina
        Maquina maquinaSalva = repository.save(maquina);

        return toResponseDTO(maquinaSalva); //como vai converter em dto, o que fica salvo no response ainda é apenas o idSetor
    }

//    //quem faz a atualização da máquina? se for o gestor ele tem acesso ao id mas se for o tecnico não
//    //precisa informar o id para atualizar e o técnico so precisa atualizar a atividade
    //restringir para o técnico as outras opções de atualização ou fazer isso apenas pela atualização da ordem?
    public MaquinaResponseDTO atualizarMaquina (Long idMaquina, MaquinaRequestDTO dto){

        Maquina maquina = buscarEntidade(idMaquina);

        maquina.setModelo(dto.modelo());
        maquina.setAtiva(dto.ativa());

        //mesma situacao do salvar
        Setor setor = setorService.buscarEntidade(dto.idSetor());
        maquina.setSetor(setor);
        Maquina maquinaSalva = repository.save(maquina);

        return toResponseDTO(maquinaSalva);
    }

    public void deletarMaquina(Long idMaquina){

        Maquina maquina = buscarEntidade(idMaquina);

        //se a máquina estiver ativa ou com ordens abertas não pode ser excluida; lança exceção **rn
        if(maquina.getAtiva() == true || !maquina.getListaOrdens().isEmpty()){
            throw new RegraDeNegocioException("Não é possível excluir máquinas ativas ou com ordens pendentes.");
        }
        repository.delete(maquina);
    }
}