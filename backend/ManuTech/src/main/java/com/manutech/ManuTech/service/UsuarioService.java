package com.manutech.ManuTech.service;


import com.manutech.ManuTech.dto.UsuarioRequestDTO;
import com.manutech.ManuTech.dto.UsuarioResponseDTO;
import com.manutech.ManuTech.exception.RegraDeNegocioException;
import com.manutech.ManuTech.model.PerfilUsuario;
import com.manutech.ManuTech.model.Tecnico;
import com.manutech.ManuTech.model.Usuario;
import com.manutech.ManuTech.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UsuarioService {


    private final UsuarioRepository repository;
    private final TecnicoService tecnicoService;
    private final PasswordEncoder passwordEncoder;


    public UsuarioService(
            UsuarioRepository repository,
            TecnicoService tecnicoService,
            PasswordEncoder passwordEncoder
    ) {
        this.repository = repository;
        this.tecnicoService = tecnicoService;
        this.passwordEncoder = passwordEncoder;
    }


    public Usuario buscarEntidade(Long idUsuario) {
        return repository.findById(idUsuario)
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado!"));
    }


    public UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getIdUsuario(),
                usuario.getLogin(),
                usuario.getPerfil(),
                usuario.getTecnico() != null ? usuario.getTecnico().getIdTecnico() : null,
                usuario.getTecnico() != null ? usuario.getTecnico().getNome() : "Acesso Gestor"
        );
    }


    public UsuarioResponseDTO salvarUsuario(UsuarioRequestDTO dto) {
        if (repository.existsByLogin(dto.login())) {
            throw new RegraDeNegocioException("Login já cadastrado.");
        }


        Usuario usuario = new Usuario();
        usuario.setLogin(dto.login());
        usuario.setSenha(passwordEncoder.encode(dto.senha()));
        usuario.setPerfil(dto.perfil());


        if (dto.perfil() == PerfilUsuario.TECNICO) {
            if (dto.idTecnico() == null) {
                throw new RegraDeNegocioException("Para o perfil TECNICO, é obrigatório informar o ID correspondente da oficina.");
            }


            Tecnico tecnico = tecnicoService.buscarEntidade(dto.idTecnico());
            usuario.setTecnico(tecnico);
        }


        Usuario usuarioSalvo = repository.save(usuario);
        return toResponseDTO(usuarioSalvo);
    }


    public void deletarUsuario(Long idUsuario) {
        Usuario usuario = buscarEntidade(idUsuario);
        repository.delete(usuario);
    }
}


