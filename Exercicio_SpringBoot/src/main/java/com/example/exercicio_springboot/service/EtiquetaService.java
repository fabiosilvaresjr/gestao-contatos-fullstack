package com.example.exercicio_springboot.service;

import com.example.exercicio_springboot.dto.EtiquetaDTO;
import com.example.exercicio_springboot.entity.Etiqueta;
import com.example.exercicio_springboot.repository.EtiquetaRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EtiquetaService {

    private final EtiquetaRepository etiquetaRepository;

    public EtiquetaService(EtiquetaRepository etiquetaRepository) {
        this.etiquetaRepository = etiquetaRepository;
    }

    // Criar (POST)
    public EtiquetaDTO salvar(EtiquetaDTO dto) {
        Etiqueta etiqueta = new Etiqueta();
        etiqueta.setNome(dto.getNome());

        Etiqueta etiquetaSalva = etiquetaRepository.save(etiqueta);

        return new EtiquetaDTO(etiquetaSalva.getId(), etiquetaSalva.getNome());
    }

    // Listar (GET) - Usando query manual
    public List<EtiquetaDTO> listarTodas() {
        return etiquetaRepository.listarTodasCustom();
    }
}