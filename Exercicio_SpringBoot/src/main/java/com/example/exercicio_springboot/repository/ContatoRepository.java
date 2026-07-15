package com.example.exercicio_springboot.repository;

import com.example.exercicio_springboot.dto.ContatoDTO;
import com.example.exercicio_springboot.entity.Contato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContatoRepository extends JpaRepository<Contato, Long> {

    // Listar todos direto mapeando pro DTO
    @Query("""
        select new com.example.exercicio_springboot.dto.ContatoDTO(
            c.id,
            c.nome,
            c.celular,
            c.favorito
        )
        from Contato c
    """)
    List<ContatoDTO> listarTodosCustom();

    // Buscar por nome
    @Query("""
        select new com.example.exercicio_springboot.dto.ContatoDTO(
            c.id,
            c.nome,
            c.celular,
            c.favorito
        )
        from Contato c
        where lower(c.nome) like lower(concat('%', :nome, '%'))
    """)
    List<ContatoDTO> buscarPorNome(@Param("nome") String nome);

    // Buscar por ID mapeando pro DTO
    @Query("""
        select new com.example.exercicio_springboot.dto.ContatoDTO(
            c.id,
            c.nome,
            c.celular,
            c.favorito
        )
        from Contato c
        where c.id = :id
    """)
    ContatoDTO buscarPorIdCustom(@Param("id") Long id);

    // Atualizar dados
    @Modifying
    @Query("""
        update Contato c
        set c.nome = :nome, c.celular = :celular, c.favorito = :favorito
        where c.id = :id
    """)
    int atualizarContato(@Param("id") Long id, @Param("nome") String nome, @Param("favorito") Boolean favorito);

    // Deletar
    @Modifying
    @Query("delete from Contato c where c.id = :id")
    void deletarPorIdCustom(@Param("id") Long id);
}