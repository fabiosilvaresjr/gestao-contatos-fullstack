package com.example.exercicio_springboot.service;

import com.example.exercicio_springboot.dto.GrupoDTO;
import com.example.exercicio_springboot.entity.Grupo;
import com.example.exercicio_springboot.repository.GrupoRepository;
import org.springframework.stereotype.Service;

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
}