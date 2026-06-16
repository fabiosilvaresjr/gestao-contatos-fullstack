package com.example.exercicio_springboot.repository;

import com.example.exercicio_springboot.model.ContatoEtiqueta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContatoEtiquetaRepository extends JpaRepository<ContatoEtiqueta, Long> {
}