package com.example.exercicio_springboot.controller;

import jakarta.validation.Valid;
import com.example.exercicio_springboot.dto.ContatoDTO;
import com.example.exercicio_springboot.service.ContatoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contatos")
@CrossOrigin(origins = "http://localhost:4200")
public class ContatoController {

    private final ContatoService contatoService;

    public ContatoController(ContatoService contatoService) {
        this.contatoService = contatoService;
    }

    // POST /contatos -> Retorna 201 Created
    @PostMapping
    public ResponseEntity<ContatoDTO> criar(@RequestBody @Valid ContatoDTO dto) {
        ContatoDTO contatoSalvo = contatoService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(contatoSalvo);
    }

    // GET /contatos -> Retorna 200 OK
    @GetMapping
    public ResponseEntity<List<ContatoDTO>> listarTodos() {
        return ResponseEntity.ok(contatoService.listarTodos()); //pode porque vai lsitar os dto do service
    }

    // GET /contatos/{id} -> Retorna 200 OK, ou exception faz o 404 Not Found
    @GetMapping("/{id}")
    public ResponseEntity<ContatoDTO> buscarPorId(@PathVariable Long id) {
        ContatoDTO dto = contatoService.buscarPorId(id);
        return ResponseEntity.ok(dto); // Retorna 200
    }

    // PATCH /contatos/{id} -> Retorna 200 OK (Usando PATCH conforme o exercício)
    @PatchMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable Long id, @Valid @RequestBody ContatoDTO dto) {
        contatoService.atualizar(id, dto);
        return ResponseEntity.ok().build();
    }

    // DELETE /contatos/{id} -> Retorna 204 No Content
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        contatoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}