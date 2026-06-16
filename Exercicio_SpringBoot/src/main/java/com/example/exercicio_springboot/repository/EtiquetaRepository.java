package com.example.exercicio_springboot.repository;

import com.example.exercicio_springboot.model.Etiqueta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EtiquetaRepository extends JpaRepository<Etiqueta, Long> {
}