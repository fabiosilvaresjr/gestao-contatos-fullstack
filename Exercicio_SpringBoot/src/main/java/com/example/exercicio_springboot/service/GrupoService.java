package com.example.exercicio_springboot.service;

import com.example.exercicio_springboot.dto.GrupoDTO;
import com.example.exercicio_springboot.entity.Grupo;
import com.example.exercicio_springboot.repository.GrupoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GrupoService {

    private final GrupoRepository grupoRepository;

    public GrupoService(GrupoRepository grupoRepository) {
        this.grupoRepository = grupoRepository;
    }

    public GrupoDTO salvar(GrupoDTO dto) {
        Grupo grupo = new Grupo();
        grupo.setNome(dto.getNome());

        Grupo grupoSalvo = grupoRepository.save(grupo);

        return new GrupoDTO(grupoSalvo.getId(), grupoSalvo.getNome());
    }

    public List<GrupoDTO> listarTodos() {
        return grupoRepository.listarTodosCustom();
    }

    public GrupoDTO buscarPorId(Long id) {
        // CORREÇÃO: grupoRepository com 'g' minúsculo (usando a instância injetada)
        GrupoDTO dto = grupoRepository.buscarPorIdCustom(id);
        if (dto == null) {
            // Se não tiver ID, GlobalExceptionHandler gera 404
            throw new EntityNotFoundException("Grupo não encontrado");
        }
        return dto;
    }

    @Transactional
    public void atualizar(Long id, GrupoDTO dto) {
        // se != grupo, o buscarPorId vai dar erro
        GrupoDTO grupoExistente = buscarPorId(id);

        String nomeAtualizado = dto.getNome() != null ? dto.getNome() : grupoExistente.getNome();

        grupoRepository.atualizarGrupo(id, nomeAtualizado);
    }

    @Transactional
    public void deletar(Long id) {
        // Usa buscarPorId para testar se existe. Se não existir, dá 404 automático
        buscarPorId(id);

        grupoRepository.deletarPorIdCustom(id);
    }
}