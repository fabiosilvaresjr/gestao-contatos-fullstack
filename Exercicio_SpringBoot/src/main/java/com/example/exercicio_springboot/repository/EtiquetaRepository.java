package com.example.exercicio_springboot.repository;

import com.example.exercicio_springboot.dto.EtiquetaDTO;
import com.example.exercicio_springboot.entity.Etiqueta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EtiquetaRepository extends JpaRepository<Etiqueta, Long> {

    // Lista atributos especificos apra DTO
    @Query("""
        select new com.example.exercicio_springboot.dto.EtiquetaDTO(
            e.id,
            e.nome
        )
        from Etiqueta e
    """)
    List<EtiquetaDTO> listarTodasCustom();
}

