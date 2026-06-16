package com.example.exercicio_springboot.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "contato_etiqueta")
@Data
public class ContatoEtiqueta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contato_id")
    private Contato contato;

    @ManyToOne
    @JoinColumn(name = "etiqueta_id")
    private Etiqueta etiqueta;
}
