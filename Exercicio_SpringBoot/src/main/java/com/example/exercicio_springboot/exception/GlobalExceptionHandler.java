package com.example.exercicio_springboot.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Captura os erros das anotações @Valid e @NotBlank
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarErro400(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(erros.stream().map(DadosErroValidacao::new).toList());
    }

    // Erro 404
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tratarErro404() {
        return ResponseEntity.notFound().build();
    }

    //  Erro 403
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<MensagemErro> tratarErro403(AccessDeniedException ex) {

        var erro = new MensagemErro("Acesso Negado", "Você não tem permissão para realizar esta ação.");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erro);
    }

    // DTO interno para os erros do @Valid
    private record DadosErroValidacao(String campo, String mensagem) {
        public DadosErroValidacao(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }

    // DTO interno para mensagens personalizadas
    private record MensagemErro(String erro, String mensagem) {}

    // Erro 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<MensagemErro> tratarErro500(Exception ex) {
        ex.printStackTrace();

        var erro = new MensagemErro("Erro Interno", "Ocorreu um erro inesperado no servidor. Tente novamente mais tarde.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }
}