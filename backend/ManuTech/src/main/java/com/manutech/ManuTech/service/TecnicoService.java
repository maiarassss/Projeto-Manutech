package com.manutech.ManuTech.service;

import com.manutech.ManuTech.dto.TecnicoRequestDTO;
import com.manutech.ManuTech.dto.TecnicoResponseDTO;
import com.manutech.ManuTech.exception.RecursoNaoEncontradoException;
import com.manutech.ManuTech.model.Tecnico;
import com.manutech.ManuTech.repository.TecnicoRepository;

import java.util.List;

public class TecnicoService {
    private final TecnicoRepository repository;

    public TecnicoService(TecnicoRepository repository) {
        this.repository = repository;
    }

    public TecnicoResponseDTO salvar(TecnicoRequestDTO dto) {
        Tecnico tecnico = new Tecnico();
        tecnico.setNome(dto.nome());
        tecnico.setTelefone(dto.telefone());
        Tecnico salvo = (Tecnico) this.repository.save(tecnico);
        return this.toResponseDTO(salvo);
    }

    private TecnicoResponseDTO toResponseDTO(Tecnico tecnico) {
        return new TecnicoResponseDTO(tecnico.getIdTecnico(), tecnico.getNome(), tecnico.getTelefone());
    }

    public List<TecnicoResponseDTO> listar() {
        return this.repository.findAll().stream().map(this::toResponseDTO).toList();
    }
    public TecnicoResponseDTO buscarPorId(Long idTecnico) {
        Tecnico tecnico = (Tecnico)this.repository.findById(idTecnico).orElseThrow(() -> new RecursoNaoEncontradoException("Tecnico não encontrado"));
        return this.toResponseDTO(tecnico);
    }
    public Tecnico buscarEntidade(Long idTecnico) {
        return (Tecnico)this.repository.findById(idTecnico).orElseThrow(() -> new RecursoNaoEncontradoException("Tecnico não encontrado"));
    }
    public TecnicoResponseDTO atualizar(Long idTecnico, TecnicoRequestDTO dto) {
        Tecnico tecnico = (Tecnico)this.repository.findById(idTecnico).orElseThrow(() -> new RecursoNaoEncontradoException("Tecnico não encontrado"));
        tecnico.setNome(dto.nome());
        tecnico.setTelefone(dto.telefone());
        Tecnico atualizado = (Tecnico)this.repository.save(tecnico);
        return this.toResponseDTO(atualizado);
    }
    public void deletar(Long idTecnico) {
        Tecnico tecnico = (Tecnico)this.repository.findById(idTecnico).orElseThrow(() -> new RecursoNaoEncontradoException("Tecnico não encontrado"));
        this.repository.delete(tecnico);
    }
}
