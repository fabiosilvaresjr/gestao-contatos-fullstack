package com.example.exercicio_springboot.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class EtiquetaDTO {

    private Long id;

    @NotBlank(message = "O nome da etiqueta é obrigatório.")
    private String nome;

    // Construtor com argumentos (Obrigatório para Query no GET)
    public EtiquetaDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }
}