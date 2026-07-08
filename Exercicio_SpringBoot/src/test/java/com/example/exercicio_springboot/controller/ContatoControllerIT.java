package com.example.exercicio_springboot.controller;

import com.example.exercicio_springboot.dto.ContatoDTO;
import com.example.exercicio_springboot.entity.Etiqueta;
import com.example.exercicio_springboot.repository.ContatoRepository;
import com.example.exercicio_springboot.repository.UsuarioRepository;
import com.example.exercicio_springboot.repository.EtiquetaRepository;
import com.example.exercicio_springboot.service.ContatoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.client.JdkClientHttpRequestFactory;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

// Requisito: Sobe o Spring de verdade em uma porta aleatória para não chocar com o Tomcat local
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
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

    @Autowired
    private ContatoService contatoService;

    @Autowired
    private EtiquetaRepository etiquetaRepository;

    @BeforeEach
    void setUp() {
        restTemplate.getRestTemplate().setRequestFactory(new JdkClientHttpRequestFactory());
        String baseUrl = "http://localhost:" + port;

        // 1. Criar um usuário ADMIN para o teste
        Map<String, String> registerRequest = Map.of(
                "login", "admin_teste",
                "password", "senha123",
                "role", "ADMIN"
        );
        restTemplate.postForEntity(baseUrl + "/auth/register", registerRequest, Void.class);

        // 2. Fazer login com esse usuário para pegar o token
        Map<String, String> loginRequest = Map.of(
                "login", "admin_teste",
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
        contatoRepository.deleteAll(); // Ajuste para o seu metodo de deletar tudo se necessário
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

    @Test
    void deveRetornar404AoBuscarContatoInexistente() {
        // Arrange: Prepara um ID falso (Usei um UUID aleatório. Se o seu banco usar Long, troque para algo como "99999")
        String idFalso = "99999";
        String url = "http://localhost:" + port + "/contatos/" + idFalso;

        // Arrange: Como é um GET com autenticação, precisamos montar o Header
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenAdmin);
        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);

        // Act: Dispara o GET real usando o metodo exchange (necessário para enviar Headers no GET)
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );

        // Assert: Verifica se a API respondeu estritamente com 404 Not Found
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deveAtualizarCampoBooleanComPatchEVerificarNoBanco() {
        // Arrange 1: Criar e salvar o contato inicial
        com.example.exercicio_springboot.entity.Contato contato = new com.example.exercicio_springboot.entity.Contato();
        contato.setNome("Contato para PATCH");
        contato.setFavorito(false);
        var contatoSalvo = contatoRepository.save(contato);

        String url = "http://localhost:" + port + "/contatos/" + contatoSalvo.getId();

        // Arrange 2: Configurar cabeçalhos e o motor HTTP correto para PATCH
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenAdmin);
        headers.setContentType(MediaType.APPLICATION_JSON);
        restTemplate.getRestTemplate().setRequestFactory(new org.springframework.http.client.JdkClientHttpRequestFactory());

        // Prepara o DTO ou Map com as alterações.
        // Como seu Controller espera um ContatoDTO, mandamos o valor atualizado
        Map<String, Object> patchRequest = Map.of(
                "nome", "Contato para PATCH",
                "favorito", true
        );
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(patchRequest, headers);

        // Act: Dispara o PATCH esperando Void (exatamente o que seu Controller retorna!)
        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.PATCH,
                requestEntity,
                Void.class
        );

        // Assert 1: Verifica se a API respondeu com 200 OK
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Assert 2: Valida se a persistência no banco funcionou de verdade
        var contatoAtualizadoNoBanco = contatoRepository.findById(contatoSalvo.getId()).orElseThrow();
        assertThat(contatoAtualizadoNoBanco.getFavorito()).isTrue();
    }

    @Test
    void deveAssociarEtiquetaAoContatoEVerificarNoBanco() {
        // Criar o Contato e a Etiqueta no banco
        ContatoDTO contatoSalvo = contatoService.salvar(new ContatoDTO(null, "João do Teste", false));
        // Assumindo que você tenha um metodo salvar na etiquetaService, ou salve direto no repository:
        Etiqueta etiqueta = new Etiqueta();
        etiqueta.setNome("Cliente VIP");
        etiqueta = etiquetaRepository.save(etiqueta);

        String url = "http://localhost:" + port + "/contatos/" + contatoSalvo.getId() + "/etiquetas/" + etiqueta.getId();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenAdmin);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        // Act: Dispara o PATCH para associar
        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.PATCH,
                requestEntity,
                Void.class
        );

        // Assert 1: Verifica se a API respondeu com 200 OK
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Assert 2: Valida se a lista de etiquetas do contato agora contém essa etiqueta
        ContatoDTO contatoAtualizado = contatoService.buscarPorId(contatoSalvo.getId());
        assertThat(contatoAtualizado.getEtiquetas()).hasSize(1);
        assertThat(contatoAtualizado.getEtiquetas().get(0).getNome()).isEqualTo("Cliente VIP");
    }

    @Test
    void deveDesassociarEtiquetaDoContato() {
        // Arrange: Prepara o terreno com contato, etiqueta e o vínculo já feito
        ContatoDTO contatoSalvo = contatoService.salvar(new ContatoDTO(null, "Maria do Teste", true));
        Etiqueta etiqueta = new Etiqueta();
        etiqueta.setNome("Fornecedor");
        etiqueta = etiquetaRepository.save(etiqueta);

        // Associa manualmente para o teste ter o que deletar
        contatoService.associarEtiqueta(contatoSalvo.getId(), etiqueta.getId());

        String url = "http://localhost:" + port + "/contatos/" + contatoSalvo.getId() + "/etiquetas/" + etiqueta.getId();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenAdmin);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        // Act: Dispara o DELETE para remover a associação
        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                requestEntity,
                Void.class
        );

        // Assert 1: Retorno deve ser 204 No Content
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // Assert 2: A lista de etiquetas do contato deve estar vazia novamente
        ContatoDTO contatoAtualizado = contatoService.buscarPorId(contatoSalvo.getId());
        assertThat(contatoAtualizado.getEtiquetas()).isEmpty();
    }

}