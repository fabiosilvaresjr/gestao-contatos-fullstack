package com.example.exercicio_springboot.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ContatoDTO {

    private Long id;

    @NotBlank(message = "O nome é obrigatório e não pode estar em branco.")
    private String nome;

    private String celular;

    private Boolean favorito;

    // Já nasce como uma lista vazia para evitar NullPointerException
    private List<EtiquetaDTO> etiquetas = new ArrayList<>();

    // Construtor 1: Usado quando busca SÓ o contato no banco (sem as etiquetas)
    public ContatoDTO(Long id, String nome, String celular, Boolean favorito) {
        this.id = id;
        this.nome = nome;
        this.celular = celular;
        this.favorito = favorito;
    }

    // Construtor 2: Usado quando fizer o JOIN no banco e já tiver a lista pronta
    public ContatoDTO(Long id, String nome, String celular, Boolean favorito, List<EtiquetaDTO> etiquetas) {
        this.id = id;
        this.nome = nome;
        this.celular = celular;
        this.favorito = favorito;
        this.etiquetas = etiquetas != null ? etiquetas : new ArrayList<>();
    }
}