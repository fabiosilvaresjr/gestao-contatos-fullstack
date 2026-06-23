package com.example.exercicio_springboot.service;

import com.example.exercicio_springboot.dto.ContatoDTO;
import com.example.exercicio_springboot.entity.Contato;
import com.example.exercicio_springboot.repository.ContatoRepository;
import jakarta.persistence.EntityNotFoundException; // <-- Importante adicionar isso!
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContatoService {

    private final ContatoRepository contatoRepository;

    public ContatoService(ContatoRepository contatoRepository) {
        this.contatoRepository = contatoRepository;
    }

    public ContatoDTO salvar(ContatoDTO dto) {
        // O @Valid do Controller cuida para nao ser nulo ou vazio
        Contato contato = new Contato();
        contato.setNome(dto.getNome());
        contato.setFavorito(dto.getFavorito() != null ? dto.getFavorito() : false);

        Contato contatoSalvo = contatoRepository.save(contato);
        return new ContatoDTO(contatoSalvo.getId(), contatoSalvo.getNome(), contatoSalvo.getFavorito());
    }

    public ContatoDTO buscarPorId(Long id) {
        ContatoDTO dto = contatoRepository.buscarPorIdCustom(id);
        if (dto == null) {
            // Se não tiver ID, GlobalExceptionHandler vai pegar e dar 404
            throw new EntityNotFoundException("Contato não encontrado");
        }
        return dto;
    }

    @Transactional
    public void atualizar(Long id, ContatoDTO dto) {
        // se != contato, o buscarPorId vai dar erro
        ContatoDTO contatoExistente = buscarPorId(id);

        String nomeAtualizado = dto.getNome() != null ? dto.getNome() : contatoExistente.getNome();
        Boolean favoritoAtualizado = dto.getFavorito() != null ? dto.getFavorito() : contatoExistente.getFavorito();

        contatoRepository.atualizarContato(id, nomeAtualizado, favoritoAtualizado);
    }

    public List<ContatoDTO> listarTodos() {
        return contatoRepository.listarTodosCustom();
    }

    @Transactional
    public void deletar(Long id) {
        // Usa buscarPorId para testar se existe. Se não existir, dá 404 automático
        buscarPorId(id);

        contatoRepository.deletarPorIdCustom(id);
    }
}