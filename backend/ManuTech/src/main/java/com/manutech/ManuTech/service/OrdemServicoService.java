package com.manutech.ManuTech.service;

import com.manutech.ManuTech.dto.OrdemServicoRequestDTO;
import com.manutech.ManuTech.dto.OrdemServicoResponseDTO;
import com.manutech.ManuTech.exception.RecursoNaoEncontradoException;
import com.manutech.ManuTech.model.Maquina;
import com.manutech.ManuTech.model.OrdemServico;
import com.manutech.ManuTech.model.Prioridade;
import com.manutech.ManuTech.model.StatusOrdem;
import com.manutech.ManuTech.repository.OrdemServicoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;

@Service
public class OrdemServicoService {

    private final OrdemServicoRepository repository;
    private final MaquinaService maquinaService;
    private final TecnicoService tecnicoService;

    public OrdemServicoService(OrdemServicoRepository repository, MaquinaService maquinaService, TecnicoService tecnicoService) {
        this.repository = repository;
        this.maquinaService = maquinaService;
        //a ordem precisa de uma máquina para ser criada
        this.tecnicoService = tecnicoService;
    }

    public OrdemServicoResponseDTO toResponseDTO(OrdemServico ordem) {
        return new OrdemServicoResponseDTO(
                //dados da ordem
                ordem.getIdOrdem(),
                ordem.getTitulo(),
                ordem.getDescricao(),
                ordem.getPrioridade(),
                ordem.getStatus(),

                //dados da máquina que a ordem atende
                ordem.getMaquina().getIdMaquina(),
                ordem.getMaquina().getCodigoIdentificador(),
                ordem.getMaquina().getModelo(),
                ordem.getMaquina().getAtiva(),

                //dados do setor que a máquina pertence
                ordem.getMaquina().getSetor().getIdSetor(),
                ordem.getMaquina().getSetor().getNomeSetor(),

                //verifica se há tecnico atrelado a ordem, se houver traz os dados dele
                ordem.getTecnico() != null ? ordem.getTecnico().getIdTecnico() : null,
                ordem.getTecnico() != null ? ordem.getTecnico().getNome() : "Não atribuído"
        );
    }

    public OrdemServico buscarEntidade(Long idOrdem){
        return repository.findById(idOrdem)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ordem não encontrada!"));
    }

    public OrdemServicoResponseDTO buscarPorId(Long idOrdem){
        OrdemServico ordem = buscarEntidade(idOrdem);

        return toResponseDTO(ordem);
    }

    public List<OrdemServicoResponseDTO> buscarPorTitulo(String titulo) {

        List<OrdemServico> ordens = repository.findByTituloContainingIgnoreCase(titulo);

        if (ordens.isEmpty()) {
            throw new RecursoNaoEncontradoException("Ordem não encontrada.");
        }

        return ordens.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<OrdemServicoResponseDTO> buscarPorPrioridade(Prioridade prioridade){

        return repository.findByPrioridade(prioridade)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<OrdemServicoResponseDTO> buscarPorStatus(StatusOrdem status){

        return repository.findByStatus(status)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }


    public List<OrdemServicoResponseDTO> listarOrdens(){
        return repository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    //listagem de ordens de determinada máquina através de seu codigo identificador
    public List<OrdemServicoResponseDTO> listarOrdensPorMaquina(String codigoIdentificador){

        List<OrdemServico> ordens = repository.findByMaquinaCodigoIdentificadorContainingIgnoreCase(codigoIdentificador);

        if (ordens.isEmpty()) {
            throw new RecursoNaoEncontradoException("Ordem não encontrada.");
        }

        return ordens.stream()
                .map(this::toResponseDTO)
                .toList();
    }


    //puxa o nome do setor através da maquina associada, e traz as ordens de máquinas desse mesmo setor
    public List<OrdemServicoResponseDTO> listarOrdensPorSetor(String nomeSetor){
        List<OrdemServico> ordens = repository.findByMaquinaSetorNomeSetorContainingIgnoreCase(nomeSetor);

        if (ordens.isEmpty()) {
            throw new RecursoNaoEncontradoException("Ordem não encontrada.");
        }

        return ordens.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<OrdemServicoResponseDTO> listarOrdensPorTecnico(Long idTecnico) {
        tecnicoService.buscarEntidade(idTecnico);
        //se o tecnico não existir ja lança o erro

        List<OrdemServico> ordens = repository.findByTecnicoIdTecnico(idTecnico);

        if (ordens.isEmpty()) {
            throw new RecursoNaoEncontradoException("Ordem não encontrada.");
        }

        return ordens.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    //atualiza a atividade da maquina conforme o status
    private void atualizarAtividade(Maquina maquina, StatusOrdem statusOrdem) {

        if (statusOrdem == StatusOrdem.ANDAMENTO) {
            maquina.setAtiva(false);
            //se a manutenção estiver em andamento a maquina é desativada

        } else { //senão, obviamente esta aberta, concluida ou cancelada = maquina funcionando
            maquina.setAtiva(true);
        }
        maquinaService.salvarEntidade(maquina);
    }


    public OrdemServicoResponseDTO salvarOrdem(OrdemServicoRequestDTO dto){

        OrdemServico ordem = new OrdemServico();

        ordem.setTitulo(dto.titulo());
        ordem.setDescricao(dto.descricao());
        ordem.setPrioridade(dto.prioridade());
        ordem.setStatus(dto.status());

        Maquina maquina = maquinaService.buscarEntidade(dto.idMaquina());
        ordem.setMaquina(maquina); //a entidade ordem recebe um objeto maquina

        atualizarAtividade(maquina, dto.status());

        if (dto.idTecnico() != null) {
            ordem.setTecnico(tecnicoService.buscarEntidade(dto.idTecnico()));
            //se a ordem for criada já com um tecnico responsavel, verifica se o id informado existe no banco e traz ele caso existir
        }

        OrdemServico ordemAtualizada = repository.save(ordem);
        return toResponseDTO(ordemAtualizada);
    }


    public OrdemServicoResponseDTO atualizarOrdem(Long idOrdem, OrdemServicoRequestDTO dto){

        OrdemServico ordem = buscarEntidade(idOrdem);

        ordem.setTitulo(dto.titulo());
        ordem.setDescricao(dto.descricao());
        ordem.setPrioridade(dto.prioridade());
        ordem.setStatus(dto.status());

        Maquina maquina = maquinaService.buscarEntidade(dto.idMaquina());
        ordem.setMaquina(maquina);

        atualizarAtividade(maquina, dto.status());

        if (dto.idTecnico() != null) {
            ordem.setTecnico(tecnicoService.buscarEntidade(dto.idTecnico()));
            //caso um id seja informado no request vai verificar o banco e atribuir
        } else {
            ordem.setTecnico(null);
            //agora caso nenhum id seja informado mantém sem tecnico atribuido ou mantem o tecnico que ja estava vinculado caso haja(testar)
        }

        OrdemServico ordemAtualizada = repository.save(ordem);
        return toResponseDTO(ordemAtualizada);
    }


    //metodo de deletar substituido por status cancelado, persistência etc
}
