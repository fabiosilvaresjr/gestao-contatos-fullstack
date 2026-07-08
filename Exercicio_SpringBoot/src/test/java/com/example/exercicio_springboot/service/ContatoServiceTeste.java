package com.example.exercicio_springboot.service;

import com.example.exercicio_springboot.dto.ContatoDTO;
import com.example.exercicio_springboot.entity.Contato;
import com.example.exercicio_springboot.repository.ContatoEtiquetaRepository;
import com.example.exercicio_springboot.repository.ContatoRepository;
import com.example.exercicio_springboot.repository.EtiquetaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Não sobe o Spring, apenas o Mockito
class ContatoServiceTest {

    @Mock
    private ContatoRepository contatoRepository; // fictício

    @Mock
    private EtiquetaRepository etiquetaRepository;

    @Mock
    private ContatoEtiquetaRepository contatoEtiquetaRepository;

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

    // Sucesso na criação (verificar chamada de save e retorno)
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
            contatoService.buscarPorId(99L); // Altere para o nome do seu metodo
        });

        // Verifica se tentou buscar no banco
        verify(contatoRepository, times(1)).buscarPorIdCustom(99L);
    }

    // Listar todos os contatos
    @Test
    void deveListarTodosOsContatos() {
        // Arrange: Ensina o banco a retornar uma lista com nosso DTO de mentira
        when(contatoRepository.listarTodosCustom()).thenReturn(List.of(contatoDTO));

        // Act: Executa o metodo
        List<ContatoDTO> resultado = contatoService.listarTodos();

        // Assert: Verifica se a lista não está vazia e tem o tamanho certo
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("João da Silva", resultado.get(0).getNome());

        verify(contatoRepository, times(1)).listarTodosCustom();
    }

    // Atualizar com Sucesso
    @Test
    void deveAtualizarContatoComSucesso() {
        // Arrange
        Long id = 1L;
        ContatoDTO dtoAtualizado = new ContatoDTO();
        dtoAtualizado.setNome("Nome Modificado");
        dtoAtualizado.setFavorito(true);

        // Verifica se o contato existe antes de atualizar
        when(contatoRepository.buscarPorIdCustom(id)).thenReturn(contatoDTO);

        // Act
        contatoService.atualizar(id, dtoAtualizado);

        // Assert: Verifica se o repositório foi chamado passando exatamente os dados novos
        verify(contatoRepository, times(1)).atualizarContato(id, "Nome Modificado", true);
    }

    // Deletar com Sucesso
    @Test
    void deveDeletarContatoComSucesso() {
        // Arrange
        Long id = 1L;

        // Verifica se o contato existe antes de apagar
        when(contatoRepository.buscarPorIdCustom(id)).thenReturn(contatoDTO);

        // Act
        contatoService.deletar(id);

        // Assert: Garante que a instrução de deletar no banco foi disparada
        verify(contatoRepository, times(1)).deletarPorIdCustom(id);
    }

}