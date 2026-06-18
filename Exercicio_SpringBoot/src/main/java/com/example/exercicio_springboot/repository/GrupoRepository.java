package com.example.exercicio_springboot.repository;

import com.example.exercicio_springboot.entity.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrupoRepository extends JpaRepository<Grupo, Long>{ }
