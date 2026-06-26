package com.example.exercicio_springboot.controller;

import com.example.exercicio_springboot.dto.EtiquetaDTO;
import com.example.exercicio_springboot.dto.GrupoDTO;
import com.example.exercicio_springboot.service.EtiquetaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/etiquetas")
@CrossOrigin(origins = "http://localhost:4200")
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

    // GET /etiquetas/{id} -> Retorna 200 OK, ou exception faz o 404 Not Found
    @GetMapping("/{id}")
    public ResponseEntity<EtiquetaDTO> buscarPorId(@PathVariable Long id) {
        EtiquetaDTO dto = etiquetaService.buscarPorId(id);
        return ResponseEntity.ok(dto); // Retorna 200
    }

    // PATCH /etiquetas/{id} -> Retorna 200 OK (Usando PATCH conforme o exercício)
    @PatchMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable Long id, @Valid @RequestBody EtiquetaDTO dto) {
        etiquetaService.atualizar(id, dto);
        return ResponseEntity.ok().build();
    }

    // DELETE /etiquetas/{id} -> Retorna 204 No Content
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        etiquetaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}