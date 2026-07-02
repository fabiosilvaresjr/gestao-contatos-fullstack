package com.example.exercicio_springboot.controller;

import com.example.exercicio_springboot.dto.ContatoDTO;
import com.example.exercicio_springboot.repository.ContatoRepository;
import com.example.exercicio_springboot.repository.UsuarioRepository; // Ajuste para o nome real do seu repositório de usuários
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

// Requisito: Sobe o Spring de verdade em uma porta aleatória para não chocar com o Tomcat local
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ContatoControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate; // O nosso "Postman" automatizado

    @Autowired
    private ContatoRepository contatoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository; // Para limpar os usuários de teste

    private String tokenAdmin;

    @BeforeEach
    void setUp() {
        String baseUrl = "http://localhost:" + port;

        // 1. Criar um usuário ADMIN para o teste
        Map<String, String> registerRequest = Map.of(
                "login", "admin_teste@email.com",
                "password", "senha123",
                "role", "ADMIN"
        );
        restTemplate.postForEntity(baseUrl + "/auth/register", registerRequest, Void.class);

        // 2. Fazer login com esse usuário para pegar o token
        Map<String, String> loginRequest = Map.of(
                "login", "admin_teste@email.com",
                "password", "senha123"
        );

        // Ajuste o tipo de retorno (ex: Map.class ou uma classe LoginResponse se você tiver)
        // para capturar onde está a String do Token. Exemplo genérico:
        ResponseEntity<Map> loginResponse = restTemplate.postForEntity(baseUrl + "/auth/login", loginRequest, Map.class);

        // Supondo que seu login devolve um JSON tipo { "token": "ey..." }
        if (loginResponse.getBody() != null && loginResponse.getBody().containsKey("token")) {
            this.tokenAdmin = loginResponse.getBody().get("token").toString();
        }
    }

    @AfterEach
    void tearDown() {
        // Requisito: Limpar o banco após cada teste para o próximo rodar limpo
        contatoRepository.deleteAll(); // Ajuste para o seu método de deletar tudo se necessário
        usuarioRepository.deleteAll();
    }

    @Test
    void deveCriarContatoComSucessoEEnviarToken() {
        String baseUrl = "http://localhost:" + port + "/contatos";

        // Arrange: Prepara o JSON do contato
        ContatoDTO novoContato = new ContatoDTO(null, "Carlos Eduardo", false);

        // Arrange: Configura o cabeçalho HTTP com o Bearer Token obtido no setUp
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenAdmin);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ContatoDTO> requestEntity = new HttpEntity<>(novoContato, headers);

        // Act: Dispara a requisição real de POST
        ResponseEntity<ContatoDTO> response = restTemplate.postForEntity(baseUrl, requestEntity, ContatoDTO.class);

        // Assert: Verifica se retornou 201 Created
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getNome()).isEqualTo("Carlos Eduardo");

        // Assert extra: Vai direto no banco ver se gravou mesmo!
        var contatosNoBanco = contatoRepository.listarTodosCustom();
        assertThat(contatosNoBanco).hasSize(1);
    }
}