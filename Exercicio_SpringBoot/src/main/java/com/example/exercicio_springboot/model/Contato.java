package com.example.exercicio_springboot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
public class Contato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nome;

    private Boolean favorito = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupos_id")
    private Grupo grupo;

    @ManyToMany
    @JoinTable(
            name = "contato_etiqueta",
            joinColumns = @JoinColumn(name = "contato_id"),
            inverseJoinColumns = @JoinColumn(name = "etiqueta_id")
    )
    private List<Etiqueta> etiquetas;
}

