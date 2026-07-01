package com.example.exercicio_springboot.service;

import com.example.exercicio_springboot.dto.ContatoDTO;
import com.example.exercicio_springboot.entity.Contato;
import com.example.exercicio_springboot.repository.ContatoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Não sobe o Spring, apenas o Mockito
class ContatoServiceTest {

    @Mock
    private ContatoRepository contatoRepository; // fictício

    @InjectMocks
    private ContatoService contatoService; // Classe teste

    private Contato contatoEntity;
    private ContatoDTO contatoDTO;

    // Organizar a classe para o teste
    @BeforeEach
    void setUp() {
        contatoDTO = new ContatoDTO();
        contatoDTO.setNome("João da Silva");
        contatoDTO.setFavorito(false);

        contatoEntity = new Contato();
        contatoEntity.setId(1L);
        contatoEntity.setNome("João da Silva");
        contatoEntity.setFavorito(false);
    }

    // Cenário 1: Sucesso na criação (verificar chamada de save e retorno)
    @Test
    void deveSalvarContatoComSucesso() {
        // Arrange (Configuração do Stub)
        when(contatoRepository.save(any(Contato.class))).thenReturn(contatoEntity);

        // Act (Ação)
        var salvo = contatoService.salvar(contatoDTO); // Altere para o nome do seu método

        // Assert (Verificações)
        assertNotNull(salvo);
        assertEquals("João da Silva", salvo.getNome());
        assertEquals(false, salvo.getFavorito());

        // Verifica se o repository.save foi chamado exatamente 1 vez
        verify(contatoRepository, times(1)).save(any(Contato.class));
    }

    // Texto em branco
    @Test
    void deveLancarExcecaoQuandoNomeEstiverEmBranco() {
        // Arrange
        contatoDTO.setNome(""); // Deixando o texto em branco

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            contatoService.salvar(contatoDTO);
        });

        // Verifica que o save NUNCA foi chamado no banco porque deu erro antes
        verify(contatoRepository, never()).save(any());
    }

    // Busca por id inexistente (EntityNotFoundException)
    @Test
    void deveLancarExcecaoQuandoBuscarPorIdInexistente() {
        // Arrange (Dizendo para o banco falso não encontrar nada)
        when(contatoRepository.buscarPorIdCustom(99L)).thenReturn(null);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            contatoService.buscarPorId(99L); // Altere para o nome do seu método
        });

        // Verifica se tentou buscar no banco
        verify(contatoRepository, times(1)).buscarPorIdCustom(99L);
    }
}