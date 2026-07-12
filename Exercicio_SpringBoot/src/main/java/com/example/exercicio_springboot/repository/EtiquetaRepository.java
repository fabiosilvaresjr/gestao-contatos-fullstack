package com.example.exercicio_springboot.repository;

import com.example.exercicio_springboot.dto.EtiquetaDTO;
import com.example.exercicio_springboot.dto.GrupoDTO;
import com.example.exercicio_springboot.entity.Etiqueta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    List<EtiquetaDTO> listarTodosCustom();

    // Buscar por nome
    @Query("""
        select new com.example.exercicio_springboot.dto.EtiquetaDTO(
            e.id,
            e.nome
        )
        from Etiqueta e
        where lower(e.nome) like lower(concat('%', :nome, '%'))
    """)
    List<EtiquetaDTO> buscarPorNome(@Param("nome") String nome);

    // Buscar por ID mapeando pro DTO
    @Query("""
        select new com.example.exercicio_springboot.dto.EtiquetaDTO(
            e.id,
            e.nome
        )
        from Etiqueta e
        where e.id = :id
    """)
    EtiquetaDTO buscarPorIdCustom(@Param("id") Long id);

    // Atualizar dados
    @Modifying
    @Query("""
        update Etiqueta e
        set e.nome = :nome
        where e.id = :id
    """)
    int atualizarEtiqueta(@Param("id") Long id, @Param("nome") String nome);

    // Deletar
    @Modifying
    @Query("delete from Etiqueta e where e.id = :id")
    void deletarPorIdCustom(@Param("id") Long id);

    // Buscar as etiquetas de um contato específico fazendo JOIN com a tabela intermediária
    @Query("""
        select new com.example.exercicio_springboot.dto.EtiquetaDTO(
            e.id,
            e.nome
        )
        from Etiqueta e
        join ContatoEtiqueta ce on e.id = ce.etiqueta.id
        where ce.contato.id = :contatoId
    """)
    List<EtiquetaDTO> buscarEtiquetasPorContatoId(@Param("contatoId") Long contatoId);

}



