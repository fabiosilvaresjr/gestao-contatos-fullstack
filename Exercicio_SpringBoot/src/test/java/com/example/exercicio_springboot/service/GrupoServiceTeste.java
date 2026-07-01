package com.example.exercicio_springboot.service;

import com.example.exercicio_springboot.dto.GrupoDTO;
import com.example.exercicio_springboot.entity.Grupo;
import com.example.exercicio_springboot.repository.GrupoRepository;
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
class GrupoServiceTest {

    @Mock
    private GrupoRepository grupoRepository; // fictício

    @InjectMocks
    private GrupoService grupoService; // Classe teste

    private Grupo grupoEntity;
    private GrupoDTO grupoDTO;

    // Organizar a classe para o teste
    @BeforeEach
    void setUp() {
        grupoDTO = new GrupoDTO();
        grupoDTO.setNome("Familia");

        grupoEntity = new Grupo();
        grupoEntity.setId(1L);
        grupoEntity.setNome("Familia");
    }

    // Sucesso na criação (verificar chamada de save e retorno)
    @Test
    void deveSalvarGrupoComSucesso() {
        // Arrange (Configuração do Stub)
        when(grupoRepository.save(any(Grupo.class))).thenReturn(grupoEntity);

        // Act (Ação)
        var salvo = grupoService.salvar(grupoDTO); // Altere para o nome do seu metodo

        // Assert (Verificações)
        assertNotNull(salvo);
        assertEquals("Familia", salvo.getNome());

        // Verifica se o repository.save foi chamado exatamente 1 vez
        verify(grupoRepository, times(1)).save(any(Grupo.class));
    }

    // Texto em branco
    @Test
    void deveLancarExcecaoQuandoNomeEstiverEmBranco() {
        // Arrange
        grupoDTO.setNome(""); // Deixando o texto em branco

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            grupoService.salvar(grupoDTO);
        });

        // Verifica que o save NUNCA foi chamado no banco porque deu erro antes
        verify(grupoRepository, never()).save(any());
    }

    // Busca por id inexistente (EntityNotFoundException)
    @Test
    void deveLancarExcecaoQuandoBuscarPorIdInexistente() {
        // Arrange (Dizendo para o banco falso não encontrar nada)
        when(grupoRepository.buscarPorIdCustom(99L)).thenReturn(null);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            grupoService.buscarPorId(99L); // Altere para o nome do seu metodo
        });

        // Verifica se tentou buscar no banco
        verify(grupoRepository, times(1)).buscarPorIdCustom(99L);
    }

    // Listar todos os grupos
    @Test
    void deveListarTodosOsGrupos() {
        // Arrange: Ensina o banco a retornar uma lista com nosso DTO de mentira
        when(grupoRepository.listarTodosCustom()).thenReturn(List.of(grupoDTO));

        // Act: Executa o metodo
        List<GrupoDTO> resultado = grupoService.listarTodos();

        // Assert: Verifica se a lista não está vazia e tem o tamanho certo
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Familia", resultado.get(0).getNome());

        verify(grupoRepository, times(1)).listarTodosCustom();
    }

    // Atualizar com Sucesso
    @Test
    void deveAtualizarGrupoComSucesso() {
        // Arrange
        Long id = 1L;
        GrupoDTO dtoAtualizado = new GrupoDTO();
        dtoAtualizado.setNome("Nome Modificado");

        // Verifica se o grupo existe antes de atualizar
        when(grupoRepository.buscarPorIdCustom(id)).thenReturn(grupoDTO);

        // Act
        grupoService.atualizar(id, dtoAtualizado);

        // Assert: Verifica se o repositório foi chamado passando exatamente os dados novos
        verify(grupoRepository, times(1)).atualizarGrupo(id, "Nome Modificado");
    }

    // Deletar com Sucesso
    @Test
    void deveDeletarGrupoComSucesso() {
        // Arrange
        Long id = 1L;

        // Verifica se o grupo existe antes de apagar
        when(grupoRepository.buscarPorIdCustom(id)).thenReturn(grupoDTO);

        // Act
        grupoService.deletar(id);

        // Assert: Garante que a instrução de deletar no banco foi disparada
        verify(grupoRepository, times(1)).deletarPorIdCustom(id);
    }

}
