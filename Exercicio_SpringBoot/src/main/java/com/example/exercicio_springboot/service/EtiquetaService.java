package com.example.exercicio_springboot.service;

import com.example.exercicio_springboot.dto.EtiquetaDTO;
import com.example.exercicio_springboot.dto.GrupoDTO;
import com.example.exercicio_springboot.entity.Etiqueta;
import com.example.exercicio_springboot.repository.EtiquetaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EtiquetaService {

    private final EtiquetaRepository etiquetaRepository;

    public EtiquetaService(EtiquetaRepository etiquetaRepository) {
        this.etiquetaRepository = etiquetaRepository;
    }

    // Criar (POST)
    public EtiquetaDTO salvar(EtiquetaDTO dto) {
        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome é obrigatório e não pode estar em branco.");
        }

        Etiqueta etiqueta = new Etiqueta();
        etiqueta.setNome(dto.getNome());

        Etiqueta etiquetaSalva = etiquetaRepository.save(etiqueta);

        return new EtiquetaDTO(etiquetaSalva.getId(), etiquetaSalva.getNome());
    }

    // Listar (GET) - Usando query manual
    public List<EtiquetaDTO> listarTodas() {
        return etiquetaRepository.listarTodosCustom();
    }

    public EtiquetaDTO buscarPorId(Long id) {
        // CORREÇÃO: etiquetaRepository com 'e' minúsculo (usando a instância injetada)
        EtiquetaDTO dto = etiquetaRepository.buscarPorIdCustom(id);
        if (dto == null) {
            // Se não tiver ID, GlobalExceptionHandler gera 404
            throw new EntityNotFoundException("Etiqueta não encontrado");
        }
        return dto;
    }

    @Transactional
    public void atualizar(Long id, EtiquetaDTO dto) {
        // se != etiqueta, o buscarPorId vai dar erro
        EtiquetaDTO etiquetaExistente = buscarPorId(id);

        String nomeAtualizado = dto.getNome() != null ? dto.getNome() : etiquetaExistente.getNome();

        etiquetaRepository.atualizarEtiqueta(id, nomeAtualizado);
    }

    @Transactional
    public void deletar(Long id) {
        // Usa buscarPorId para testar se existe. Se não existir, dá 404 automático
        buscarPorId(id);

        etiquetaRepository.deletarPorIdCustom(id);
    }

}