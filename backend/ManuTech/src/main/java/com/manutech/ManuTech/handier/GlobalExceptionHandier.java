package com.manutech.ManuTech.handier;

import com.manutech.ManuTech.exception.RecursoNaoEncontradoException;
import com.manutech.ManuTech.exception.RegraDeNegocioException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandier {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> tratarValidacao(MethodArgumentNotValidException ex){
        Map<String, String> erros = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(
                erro -> erros.put(erro.getField(),erro.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(erros);
    }

    @ExceptionHandler (RecursoNaoEncontradoException.class)
    public ResponseEntity<Map<String, String>> tratarRecursoNaoEncontrado(RecursoNaoEncontradoException ex){
        Map<String, String> erro = new HashMap<>();
        erro.put("erro", ex.getMessage());

        return ResponseEntity.status(404).body(erro);
    }

    @ExceptionHandler (RegraDeNegocioException.class)
    public ResponseEntity<Map<String, String>> tratarRegraDeNegocio(RegraDeNegocioException ex){
        Map<String, String> erro = new HashMap<>();
        erro.put("erro", ex.getMessage());

        return ResponseEntity.badRequest().body(erro);
    }
}

