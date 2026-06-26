package com.example.exercicio_springboot.controller;

import com.example.exercicio_springboot.dto.ContatoDTO;
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

    // POST /grupos -> Retorna 201 Created
    @PostMapping
    public ResponseEntity<GrupoDTO> criar(@RequestBody @Valid GrupoDTO dto) {
        GrupoDTO grupoSalvo = grupoService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(grupoSalvo);
    }

    // GET /grupos -> Retorna 200 OK
    @GetMapping
    public ResponseEntity<List<GrupoDTO>> listarTodos() {
        return ResponseEntity.ok(grupoService.listarTodos()); //pode porque vai lsitar os dto do service
    }

    // GET /grupos/{id} -> Retorna 200 OK, ou exception faz o 404 Not Found
    @GetMapping("/{id}")
    public ResponseEntity<GrupoDTO> buscarPorId(@PathVariable Long id) {
        GrupoDTO dto = grupoService.buscarPorId(id);
        return ResponseEntity.ok(dto); // Retorna 200
    }

    // PATCH /grupos/{id} -> Retorna 200 OK (Usando PATCH conforme o exercício)
    @PatchMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable Long id, @Valid @RequestBody GrupoDTO dto) {
        grupoService.atualizar(id, dto);
        return ResponseEntity.ok().build();
    }

    // DELETE /contatos/{id} -> Retorna 204 No Content
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        grupoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}