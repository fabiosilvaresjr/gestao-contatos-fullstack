package com.example.exercicio_springboot.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor

public class GrupoDTO {

    private Long id;

    @NotBlank(message = "O nome do grupo é obrigatório.")
    private String nome;

    // Construtor para query no Repository
    public GrupoDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }
}