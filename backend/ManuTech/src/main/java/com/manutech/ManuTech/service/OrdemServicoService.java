package com.manutech.ManuTech.service;

import com.manutech.ManuTech.dto.OrdemServicoRequestDTO;
import com.manutech.ManuTech.dto.OrdemServicoResponseDTO;
import com.manutech.ManuTech.exception.RecursoNaoEncontradoException;
import com.manutech.ManuTech.model.Maquina;
import com.manutech.ManuTech.model.OrdemServico;
import com.manutech.ManuTech.model.Prioridade;
import com.manutech.ManuTech.model.StatusOrdem;
import com.manutech.ManuTech.repository.MaquinaRepository;
import com.manutech.ManuTech.repository.OrdemServicoRepository;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class OrdemServicoService {

    private final OrdemServicoRepository repository;
    private final MaquinaService maquinaService;


    public OrdemServicoService(OrdemServicoRepository repository, MaquinaService maquinaService) {
        this.repository = repository;
        this.maquinaService = maquinaService;
        //a ordem precisa de uma máquina para ser criada
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
                ordem.getMaquina().getSetor().getNomeSetor()
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

    public List<OrdemServicoResponseDTO> buscarPorTitulo(String titulo){

        return repository.findByTituloContainingIgnoreCase(titulo)
                .stream()
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
        return repository.findByMaquinaCodigoIdentificador(codigoIdentificador)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }


    //puxa o nome do setor através da maquina associada, e traz as ordens de máquinas desse mesmo setor
    public List<OrdemServicoResponseDTO> listarOrdensPorSetor(String nomeSetor){
        return repository.findByMaquinaSetorNomeSetorContainingIgnoreCase(nomeSetor)
                .stream()
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

        OrdemServico ordemAtualizada = repository.save(ordem);
        return toResponseDTO(ordemAtualizada);
    }


    //metodo de deletar substituido por status cancelado, persistência etc
}
