package com.example.exercicio_springboot.service;

import com.example.exercicio_springboot.model.Contato;
import com.example.exercicio_springboot.repository.ContatoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContatoService {

    // Injetando o repositório
    private final ContatoRepository contatoRepository;

    public ContatoService(ContatoRepository contatoRepository) {
        this.contatoRepository = contatoRepository;
    }

    // Criar ou Atualizar um contato
    public Contato salvar(Contato contato) {
        return contatoRepository.save(contato);
    }

    // Listar todos os contatos
    public List<Contato> listarTodos() {
        return contatoRepository.findAll();
    }

    // Buscar um contato específico pelo ID
    public Contato buscarPorId(Long id) {
        // Retorna o contato se achar, ou null se não achar
        return contatoRepository.findById(id).orElse(null);
    }

    // Deletar um contato
    public void deletar(Long id) {
        contatoRepository.deleteById(id);
    }
}