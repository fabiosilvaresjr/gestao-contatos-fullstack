package com.example.exercicio_springboot.dto;

import jakarta.validation.constraints.NotBlank;

public class ContatoDTO {

    private Long id;
    @NotBlank(message = "O nome é obrigatório e não pode estar em branco.")
    private String nome;
    private Boolean favorito;


    // Construtor vazio
    public ContatoDTO() {
    }

    // Construtor que recebe a Entidade do banco e extrai
    public ContatoDTO(Long id, String nome, Boolean favorito) {
        this.id = id;
        this.nome = nome;
        this.favorito = favorito;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getFavorito() {
        return favorito;
    }

    public void setFavorito(Boolean favorito) {
        this.favorito = favorito;
    }
}
