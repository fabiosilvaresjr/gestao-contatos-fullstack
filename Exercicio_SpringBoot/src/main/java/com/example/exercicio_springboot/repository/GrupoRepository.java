package com.example.exercicio_springboot.repository;

import com.example.exercicio_springboot.dto.GrupoDTO;
import com.example.exercicio_springboot.entity.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Long> {

    @Query("""
        select new com.example.exercicio_springboot.dto.GrupoDTO(
            g.id,
            g.nome
        )
        from Grupo g
    """)
    List<GrupoDTO> listarTodosCustom();
}