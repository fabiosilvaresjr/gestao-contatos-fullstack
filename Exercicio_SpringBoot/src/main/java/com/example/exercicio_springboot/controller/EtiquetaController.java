package com.example.exercicio_springboot.controller;

import com.example.exercicio_springboot.dto.EtiquetaDTO;
import com.example.exercicio_springboot.service.EtiquetaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/etiquetas")
public class EtiquetaController {

    private final EtiquetaService etiquetaService;

    public EtiquetaController(EtiquetaService etiquetaService) {
        this.etiquetaService = etiquetaService;
    }

    // POST /etiquetas -> Retorna 201 Created
    @PostMapping
    public ResponseEntity<EtiquetaDTO> criar(@RequestBody @Valid EtiquetaDTO dto) {
        EtiquetaDTO etiquetaSalva = etiquetaService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(etiquetaSalva);
    }

    // GET /etiquetas -> Retorna 200 OK
    @GetMapping
    public ResponseEntity<List<EtiquetaDTO>> listarTodas() {
        List<EtiquetaDTO> lista = etiquetaService.listarTodas();
        return ResponseEntity.ok(lista);
    }
}