package com.example.exercicio_springboot.service;

import com.example.exercicio_springboot.dto.ContatoDTO;
import com.example.exercicio_springboot.dto.EtiquetaDTO;
import com.example.exercicio_springboot.entity.Contato;
import com.example.exercicio_springboot.repository.ContatoEtiquetaRepository;
import com.example.exercicio_springboot.repository.ContatoRepository;
import com.example.exercicio_springboot.repository.EtiquetaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContatoService {

    private final ContatoRepository contatoRepository;
    private final EtiquetaRepository etiquetaRepository;
    private final ContatoEtiquetaRepository contatoEtiquetaRepository;

    // Injetando os novos repositórios
    public ContatoService(ContatoRepository contatoRepository,
                          EtiquetaRepository etiquetaRepository,
                          ContatoEtiquetaRepository contatoEtiquetaRepository) {
        this.contatoRepository = contatoRepository;
        this.etiquetaRepository = etiquetaRepository;
        this.contatoEtiquetaRepository = contatoEtiquetaRepository;
    }

    public ContatoDTO salvar(ContatoDTO dto) {
        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome é obrigatório e não pode estar em branco.");
        }

        Contato contato = new Contato();
        contato.setNome(dto.getNome());
        contato.setCelular(dto.getCelular());
        contato.setFavorito(dto.getFavorito() != null ? dto.getFavorito() : false);

        Contato contatoSalvo = contatoRepository.save(contato);
        return new ContatoDTO(contatoSalvo.getId(), contatoSalvo.getNome(), contatoSalvo.getCelular(), contatoSalvo.getFavorito());
    }

    public ContatoDTO buscarPorId(Long id) {

        ContatoDTO dto = contatoRepository.buscarPorIdCustom(id);
        if (dto == null) {
            throw new EntityNotFoundException("Contato não encontrado");
        }

        // Busca as etiquetas vinculadas ao contato
        List<EtiquetaDTO> etiquetas = etiquetaRepository.buscarEtiquetasPorContatoId(id);
        dto.setEtiquetas(etiquetas);

        return dto;
    }

    @Transactional
    public void atualizar(Long id, ContatoDTO dto) {
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
        buscarPorId(id);
        //Primeiro removendo a tabela intermediaria apara nao impedir de apagar o contato
        contatoEtiquetaRepository.removerTodosVinculosDoContato(id);

        contatoRepository.deletarPorIdCustom(id);
    }

   //Métodos da relação com as etiquetas
    @Transactional
    public void associarEtiqueta(Long contatoId, Long etiquetaId) {
        // Verifica se o contato existe (lança 404 se não)
        buscarPorId(contatoId);

        // Verifica se a etiqueta existe (lança 404 se não)
        EtiquetaDTO etiqueta = etiquetaRepository.buscarPorIdCustom(etiquetaId);
        if (etiqueta == null) {
            throw new EntityNotFoundException("Etiqueta não encontrada");
        }

        // Evitar associação duplicada
        List<EtiquetaDTO> etiquetasDoContato = etiquetaRepository.buscarEtiquetasPorContatoId(contatoId);
        boolean jaPossui = etiquetasDoContato.stream().anyMatch(e -> e.getId().equals(etiquetaId));
        if (jaPossui) {
            throw new IllegalArgumentException("O contato já possui esta etiqueta associada.");
        }

        // Dando certo, faz a relação
        contatoEtiquetaRepository.vincularEtiquetaAoContato(contatoId, etiquetaId);
    }

    @Transactional
    public void desassociarEtiqueta(Long contatoId, Long etiquetaId) {
        // Garante que o contato existe
        buscarPorId(contatoId);

        // Executa a query manual de DELETE na tabela intermediária
        contatoEtiquetaRepository.desvincularEtiquetaDoContato(contatoId, etiquetaId);
    }
}