package com.example.exercicio_springboot.repository;

import com.example.exercicio_springboot.entity.ContatoEtiqueta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContatoEtiquetaRepository extends JpaRepository<ContatoEtiqueta, Long> {

    @Modifying
    @Query(value = """
        INSERT INTO contato_etiqueta (contato_id, etiqueta_id) 
        VALUES (:contatoId, :etiquetaId)
    """, nativeQuery = true)
    void vincularEtiquetaAoContato(@Param("contatoId") Long contatoId, @Param("etiquetaId") Long etiquetaId);

    // Remover APENAS UMA etiqueta específica de um contato
    @Modifying
    @Query("""
        DELETE FROM ContatoEtiqueta ce 
        WHERE ce.contato.id = :contatoId 
        AND ce.etiqueta.id = :etiquetaId
    """)
    void desvincularEtiquetaDoContato(@Param("contatoId") Long contatoId, @Param("etiquetaId") Long etiquetaId);

    // Remover TODAS as etiquetas de um contato
    @Modifying
    @Query("""
        DELETE FROM ContatoEtiqueta ce 
        WHERE ce.contato.id = :contatoId
    """)
    void removerTodosVinculosDoContato(@Param("contatoId") Long contatoId);

    // Remover TODOS os contatos de uma etiqueta (Usado logo ANTES de deletar uma etiqueta)
    @Modifying
    @Query("""
        DELETE FROM ContatoEtiqueta ce 
        WHERE ce.etiqueta.id = :etiquetaId
    """)
    void removerTodosVinculosDaEtiqueta(@Param("etiquetaId") Long etiquetaId);
}