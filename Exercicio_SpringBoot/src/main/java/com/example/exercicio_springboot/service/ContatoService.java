package com.example.exercicio_springboot.service;

import com.example.exercicio_springboot.dto.ContatoDTO;
import com.example.exercicio_springboot.entity.Contato;
import com.example.exercicio_springboot.repository.ContatoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ContatoService {

    private final ContatoRepository contatoRepository;

    public ContatoService(ContatoRepository contatoRepository) {
        this.contatoRepository = contatoRepository;
    }

    // Regra de Negócio
    private void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            // Erro 400 Bad Request
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo nome é obrigatório e não pode estar em branco.");
        }
    }

    public ContatoDTO salvar(ContatoDTO dto) {
        validarNome(dto.getNome()); // Valida antes de tudo

        Contato contato = new Contato();
        contato.setNome(dto.getNome());
        // Favorito é default false
        contato.setFavorito(dto.getFavorito() != null ? dto.getFavorito() : false);

        Contato contatoSalvo = contatoRepository.save(contato);

        return new ContatoDTO(contatoSalvo.getId(), contatoSalvo.getNome(), contatoSalvo.getFavorito());
    }

    @Transactional
    public void atualizar(Long id, ContatoDTO dto) {
        // PATCH (validação parcial)
        if (dto.getNome() != null) {
            validarNome(dto.getNome());
        }

        ContatoDTO contatoExistente = buscarPorId(id);
        if (contatoExistente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contato não encontrado");
        }

        // Mantém o valor antigo se o novo vier nulo
        String nomeAtualizado = dto.getNome() != null ? dto.getNome() : contatoExistente.getNome();
        Boolean favoritoAtualizado = dto.getFavorito() != null ? dto.getFavorito() : contatoExistente.getFavorito();

        contatoRepository.atualizarContato(id, nomeAtualizado, favoritoAtualizado);
    }

    public List<ContatoDTO> listarTodos() {
        return contatoRepository.listarTodosCustom();
    }

    public ContatoDTO buscarPorId(Long id) {
        return contatoRepository.buscarPorIdCustom(id);
    }

    @Transactional
    public void deletar(Long id) {
        if (buscarPorId(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contato não encontrado");
        }
        contatoRepository.deletarPorIdCustom(id);
    }
}