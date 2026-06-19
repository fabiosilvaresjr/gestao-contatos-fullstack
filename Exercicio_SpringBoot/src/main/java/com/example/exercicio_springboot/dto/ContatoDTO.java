package com.example.exercicio_springboot.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class ContatoDTO {

    private Long id;

    @NotBlank(message = "O nome é obrigatório e não pode estar em branco.")
    private String nome;

    private Boolean favorito;

    // Construtor que recebe a Entidade do banco e extrai
    public ContatoDTO(Long id, String nome, Boolean favorito) {
        this.id = id;
        this.nome = nome;
        this.favorito = favorito;
    }
}