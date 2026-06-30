package com.manutech.ManuTech.controller;


import com.manutech.ManuTech.dto.LoginRequestDTO;
import com.manutech.ManuTech.dto.LoginResponseDTO;
import com.manutech.ManuTech.model.Usuario;
import com.manutech.ManuTech.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/login")
public class AuthController {


    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;


    public AuthController(
            AuthenticationManager authenticationManager,
            UsuarioRepository usuarioRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
    }


    @PostMapping
    public LoginResponseDTO login(@RequestBody @Valid LoginRequestDTO dto) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(dto.login(), dto.senha());


        authenticationManager.authenticate(authentication);


        Usuario usuario = usuarioRepository.findByLogin(dto.login())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));


        return new LoginResponseDTO(
                usuario.getIdUsuario(),
                usuario.getLogin(),
                usuario.getPerfil(),
                "Login realizado com sucesso."
        );
    }
}