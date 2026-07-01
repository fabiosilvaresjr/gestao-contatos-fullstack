# API de Gestão de Contatos

Uma API RESTful desenvolvida em **Spring Boot** para o gerenciamento de contatos, organizados por grupos e etiquetas. O sistema conta com uma infraestrutura robusta de segurança, utilizando autenticação Stateless com **JWT (JSON Web Token)**, controle de acesso baseado em perfis (Roles) e tratamento inteligente de exceções.

---

## Tecnologias Utilizadas

* **Java & Spring Boot:** Framework principal para o desenvolvimento da API.
* **Spring Security:** Gerenciamento de Autenticação e Autorização.
* **Auth0 Java-JWT:** Geração e validação de tokens JWT.
* **Spring Data JPA & Hibernate:** Mapeamento objeto-relacional e persistência de dados.
* **Flyway:** Ferramenta de migração para versionamento do banco de dados.
* **PostgreSQL / H2 Database:** Bancos de dados utilizados na aplicação.
* **Lombok:** Redução de boilerplate de código.

---

## Segurança e Regras de Acesso (RBAC)

O sistema utiliza controle de acesso baseado em Roles (`ADMIN` e `USER`). Todas as requisições (exceto login e registro) exigem o envio do token no cabeçalho `Authorization: Bearer <token>`.

### Níveis de Permissão

| Recurso | Ação (Método HTTP) | Permissão Exigida |
| :--- | :--- | :--- |
| **Autenticação** | `POST` /auth/login | Aberto a todos |
| **Autenticação** | `POST` /auth/register | Aberto a todos |
| **Contatos, Grupos, Etiquetas** | `GET` (Listar / Buscar) | Qualquer usuário logado (`USER` ou `ADMIN`) |
| **Contatos, Grupos, Etiquetas** | `POST` (Criar) | Apenas `ADMIN` |
| **Contatos, Grupos, Etiquetas** | `PUT` / `PATCH` (Atualizar) | Apenas `ADMIN` |
| **Contatos, Grupos, Etiquetas** | `DELETE` (Excluir) | Apenas `ADMIN` |

---

## Tratamento Global de Exceções

A API utiliza um interceptador global (`@RestControllerAdvice`) para capturar erros em tempo de execução e devolver respostas JSON padronizadas e amigáveis ao cliente, garantindo estabilidade e segurança.

* **Erro 400 (Bad Request):** Quando dados obrigatórios não são enviados ou chegam mal formatados no JSON (validados via `@Valid`), a API intercepta a `MethodArgumentNotValidException` e retorna uma lista limpa contendo apenas o nome do campo e a mensagem de validação.
* **Erro 403 (Forbidden):** Intercepta `AccessDeniedException` quando um usuário com perfil `USER` tenta acessar uma rota protegida (exclusiva para `ADMIN`), retornando uma mensagem clara de "Acesso Negado".
* **Erro 404 (Not Found):** Buscas por IDs ou recursos inexistentes são capturadas (`EntityNotFoundException`) e resolvidas silenciosamente para um status 404 vazio.
* **Erro 500 (Internal Server Error):** Um *catch-all* para exceções genéricas não mapeadas. Evita expor *stack traces* e detalhes da infraestrutura ao cliente, retornando um payload amigável informando erro interno.

---

## Testes Automatizados

O projeto conta com uma suíte de testes automatizados focada em garantir a qualidade e resiliência da aplicação. Os testes foram estruturados seguindo o padrão **AAA (Arrange, Act, Assert)**.

* **Testes Unitários (Regra de Negócio):** Utilizando **JUnit 5**, **Mockito** e **AssertJ**, as camadas de serviço (`ContatoService`, `GrupoService`, `EtiquetaService`) são testadas de forma isolada. Foram implementados Mocks (`@Mock`, `@InjectMocks`)  e o tratamento de exceções (ex: `EntityNotFoundException` para IDs inexistentes) funcionem corretamente sem a necessidade de instanciar o contexto do Spring ou acessar o banco de dados real.

## Como executar o projeto localmente

1. Clone o repositório para a sua máquina:
```bash
git clone <url-do-repositorio>
```

2. Abra o projeto na sua IDE de preferência (IntelliJ, Eclipse, VS Code).

3. Certifique-se de ter o Java JDK instalado (versão compatível com o projeto).

4. Configure as variáveis de ambiente necessárias no arquivo application.properties (ex: senha do banco, api.security.token.secret).

5. Execute a classe ExercicioSpringBootApplication.java.

6. A API estará disponível em http://localhost:8080.

---

## Como testar no Postman

1. Faça uma requisição POST para http://localhost:8080/auth/register enviando no Body (JSON):
```json
{
   "login": "seu_email@email.com",
   "password": "sua_senha",
   "role": "ADMIN"
}
```

2. Faça uma requisição POST para http://localhost:8080/auth/login com as mesmas credenciais para receber o seu Token JWT.

3. Copie o Token recebido.

4. Para acessar as rotas protegidas (ex: GET /contatos), vá na aba Authorization do Postman, selecione Bearer Token e cole o token copiado.