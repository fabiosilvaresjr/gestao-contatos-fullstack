package com.example.exercicio_springboot.service;

import com.example.exercicio_springboot.dto.EtiquetaDTO;
import com.example.exercicio_springboot.entity.Etiqueta;
import com.example.exercicio_springboot.repository.EtiquetaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Não sobe o Spring, apenas o Mockito
class EtiquetaServiceTest {

    @Mock
    private EtiquetaRepository etiquetaRepository; // fictício

    @InjectMocks
    private EtiquetaService etiquetaService; // Classe teste

    private Etiqueta etiquetaEntity;
    private EtiquetaDTO etiquetaDTO;

    // Organizar a classe para o teste
    @BeforeEach
    void setUp() {
        etiquetaDTO = new EtiquetaDTO();
        etiquetaDTO.setNome("Trabalho");

        etiquetaEntity = new Etiqueta();
        etiquetaEntity.setId(1L);
        etiquetaEntity.setNome("Trabalho");
    }

    // Sucesso na criação (verificar chamada de save e retorno)
    @Test
    void deveSalvarEtiquetaComSucesso() {
        // Arrange (Configuração do Stub)
        when(etiquetaRepository.save(any(Etiqueta.class))).thenReturn(etiquetaEntity);

        // Act (Ação)
        var salvo = etiquetaService.salvar(etiquetaDTO);

        // Assert (Verificações)
        assertNotNull(salvo);
        assertEquals("Trabalho", salvo.getNome());

        // Verifica se o repository.save foi chamado exatamente 1 vez
        verify(etiquetaRepository, times(1)).save(any(Etiqueta.class));
    }

    // Texto em branco
    @Test
    void deveLancarExcecaoQuandoNomeEstiverEmBranco() {
        // Arrange
        etiquetaDTO.setNome(""); // Deixando o texto em branco

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            etiquetaService.salvar(etiquetaDTO);
        });

        // Verifica que o save NUNCA foi chamado no banco porque deu erro antes
        verify(etiquetaRepository, never()).save(any());
    }

    // Busca por id inexistente (EntityNotFoundException)
    @Test
    void deveLancarExcecaoQuandoBuscarPorIdInexistente() {
        // Arrange (Dizendo para o banco falso não encontrar nada)
        when(etiquetaRepository.buscarPorIdCustom(99L)).thenReturn(null);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            etiquetaService.buscarPorId(99L);
        });

        // Verifica se tentou buscar no banco
        verify(etiquetaRepository, times(1)).buscarPorIdCustom(99L);
    }

    // Listar todas as etiquetas
    @Test
    void deveListarTodasAsEtiquetas() {
        // Arrange: Ensina o banco a retornar uma lista com nosso DTO de mentira
        when(etiquetaRepository.listarTodosCustom()).thenReturn(List.of(etiquetaDTO));

        // Act: Executa o metodo
        List<EtiquetaDTO> resultado = etiquetaService.listarTodas();

        // Assert: Verifica se a lista não está vazia e tem o tamanho certo
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Trabalho", resultado.get(0).getNome());

        verify(etiquetaRepository, times(1)).listarTodosCustom();
    }

    // Atualizar com Sucesso
    @Test
    void deveAtualizarEtiquetaComSucesso() {
        // Arrange
        Long id = 1L;
        EtiquetaDTO dtoAtualizado = new EtiquetaDTO();
        dtoAtualizado.setNome("Nome Modificado");

        // Verifica se a etiqueta existe antes de atualizar
        when(etiquetaRepository.buscarPorIdCustom(id)).thenReturn(etiquetaDTO);

        // Act
        etiquetaService.atualizar(id, dtoAtualizado);

        // Assert: Verifica se o repositório foi chamado passando exatamente os dados novos
        verify(etiquetaRepository, times(1)).atualizarEtiqueta(id, "Nome Modificado");
    }

    // Deletar com Sucesso
    @Test
    void deveDeletarEtiquetaComSucesso() {
        // Arrange
        Long id = 1L;

        // Verifica se a etiqueta existe antes de apagar
        when(etiquetaRepository.buscarPorIdCustom(id)).thenReturn(etiquetaDTO);

        // Act
        etiquetaService.deletar(id);

        // Assert: Garante que a instrução de deletar no banco foi disparada
        verify(etiquetaRepository, times(1)).deletarPorIdCustom(id);
    }
}