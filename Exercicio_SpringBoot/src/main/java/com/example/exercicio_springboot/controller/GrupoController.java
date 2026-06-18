package com.example.exercicio_springboot.controller;

import com.example.exercicio_springboot.dto.GrupoDTO;
import com.example.exercicio_springboot.service.GrupoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grupos")
public class GrupoController {

    private final GrupoService grupoService;

    public GrupoController(GrupoService grupoService) {
        this.grupoService = grupoService;
    }

    @PostMapping
    public ResponseEntity<GrupoDTO> criar(@RequestBody @Valid GrupoDTO dto) {
        GrupoDTO grupoSalvo = grupoService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(grupoSalvo);
    }

    @GetMapping
    public ResponseEntity<List<GrupoDTO>> listarTodos() {
        List<GrupoDTO> lista = grupoService.listarTodos();
        return ResponseEntity.ok(lista);
    }
}