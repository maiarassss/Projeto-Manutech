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
        this.ordemService = ordemService; //o histórico de manutenções da máquina é dependente da ordem
        this.setorService = setorService;
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

    //metodo para salvar o estado de atividade da máquina conforme o status da ordem de serviço
    public void salvarEntidade(Maquina maquina){
        repository.save(maquina);
    }

    public MaquinaResponseDTO buscarPorId(Long idMaquina){
        Maquina maquina = buscarEntidade(idMaquina);
        return toResponseDTO(maquina);
    }

    public List<MaquinaResponseDTO> buscarPorCodigo(String codigoIdentificador){

        List<Maquina> maquinas = repository.findByCodigoIdentificadorContainingIgnoreCase(codigoIdentificador);
       //guarda os resultados da busca com valor aproximado em uma lista

        if (maquinas.isEmpty()) { //verifica a existência de registros
            throw new RecursoNaoEncontradoException("Máquina não encontrada!");
        }

        return maquinas
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<MaquinaResponseDTO> buscarPorModelo(String modelo){

        List<Maquina> maquinas = repository.findByModeloContainingIgnoreCase(modelo);

        if (maquinas.isEmpty()) {
            throw new RecursoNaoEncontradoException("Máquina não encontrada!");
        }
        return maquinas
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<MaquinaResponseDTO> buscarPorModeloESetor(String modelo, Long idSetor){

        //primeiro verifica se o setor existe
        setorService.buscarEntidade(idSetor);

        List<Maquina> maquinas = repository.findByModeloAndSetorIdSetor(modelo, idSetor);

        if (maquinas.isEmpty()) {
            throw new RecursoNaoEncontradoException("Máquina não encontrada!");
        }

        return maquinas
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

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

    public List<MaquinaResponseDTO> buscarMaquinasPorSetor(Long idSetor){

        setorService.buscarEntidade(idSetor);

        return repository.findBySetorIdSetor(idSetor)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<OrdemServicoResponseDTO> listarOrdens(String codigoIdentificador){
        return ordemService.listarOrdensPorMaquina(codigoIdentificador);
    }

    public MaquinaResponseDTO salvarMaquina(MaquinaRequestDTO dto){

        Maquina maquina = new Maquina();

        //verifica se o código identificador inserido no request já existe no banco de dados
        if (repository.existsByCodigoIdentificadorIgnoreCase(dto.codigoIdentificador())) {
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

    public MaquinaResponseDTO atualizarMaquina (Long idMaquina, MaquinaRequestDTO dto){

        Maquina maquina = buscarEntidade(idMaquina);

        maquina.setModelo(dto.modelo());
        maquina.setAtiva(dto.ativa());

        Setor setor = setorService.buscarEntidade(dto.idSetor());
        maquina.setSetor(setor);
        Maquina maquinaSalva = repository.save(maquina);

        return toResponseDTO(maquinaSalva);
    }

    public void deletarMaquina(Long idMaquina){

        Maquina maquina = buscarEntidade(idMaquina);

        //se a máquina estiver ativa ou com ordens abertas não pode ser excluída
        if(maquina.getAtiva() == true || !maquina.getListaOrdens().isEmpty()){
            throw new RegraDeNegocioException("Não é possível excluir máquinas ativas ou com ordens pendentes.");
        }
        repository.delete(maquina);
    }
}