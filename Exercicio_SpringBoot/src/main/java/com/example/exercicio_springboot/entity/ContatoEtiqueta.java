package com.example.exercicio_springboot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "contato_etiqueta")
@Getter
@Setter
@NoArgsConstructor

public class ContatoEtiqueta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "contato_id")
    private Contato contato;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "etiqueta_id")
    private Etiqueta etiqueta;

    @Column(name = "data_adicao")
    private LocalDateTime dataAdicao;

    public ContatoEtiqueta(Contato contato, Etiqueta etiqueta){
        this.contato = contato;
        this.etiqueta = etiqueta;
        this.dataAdicao = LocalDateTime.now();
    }
}
