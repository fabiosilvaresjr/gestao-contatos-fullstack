package com.example.exercicio_springboot.repository;

import com.example.exercicio_springboot.dto.ContatoDTO;
import com.example.exercicio_springboot.dto.GrupoDTO;
import com.example.exercicio_springboot.entity.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    // Query buscar por nome
    @Query("""
        select new com.example.exercicio_springboot.dto.GrupoDTO(
            g.id,
            g.nome
        )
        from Grupo g
        where lower(g.nome) like lower(concat('%', :nome, '%'))
    """)
    List<GrupoDTO> buscarPorNome(@Param("nome") String nome);

    // Query para buscar por ID mapeando pro DTO
    @Query("""
        select new com.example.exercicio_springboot.dto.GrupoDTO(
            g.id,
            g.nome
        )
        from Grupo g
        where g.id = :id
    """)
    GrupoDTO buscarPorIdCustom(@Param("id") Long id);

    // Query atualizar dados
    @Modifying
    @Query("""
        update Grupo g
        set g.nome = :nome
        where g.id = :id
    """)
    int atualizarGrupo(@Param("id") Long id, @Param("nome") String nome);

    // Query deletar
    @Modifying
    @Query("delete from Grupo g where g.id = :id")
    void deletarPorIdCustom(@Param("id") Long id);
}