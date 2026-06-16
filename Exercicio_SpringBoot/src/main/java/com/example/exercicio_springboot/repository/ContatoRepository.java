package com.example.exercicio_springboot.repository;

import com.example.exercicio_springboot.model.Contato;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContatoRepository  extends JpaRepository<Contato, Long> {
}