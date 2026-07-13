# API de Gestão de Contatos

Uma aplicação Full-Stack desenvolvida para o gerenciamento de contatos, organizados por grupos e etiquetas. O sistema é construído com foco em alta performance e controle total da camada de persistência, utilizando **JDBC puro** para consultas SQL otimizadas e **Spring Security** para segurança com JWT.
---

## Tecnologias Utilizadas

### Backend (API RESTful)
* **Java & Spring Boot:** Framework principal para o desenvolvimento da API.
* **Spring Security:** Gerenciamento de Autenticação e Autorização.
* **Auth0 Java-JWT:** Geração e validação de tokens JWT.
* **Spring Data JPA & Hibernate:** Mapeamento objeto-relacional e persistência de dados.
* **Flyway:** Ferramenta de migração para versionamento do banco de dados.
* **PostgreSQL:** Banco de dados relacional utilizados na aplicação.
* **Lombok:** Redução de boilerplate de código.
* * **JUnit 5 & Mockito:** Testes automatizados focados na regra de negócio.

### Frontend (SPA)
* **Angular 17+:** Interface reativa.
* **Angular Material:** Componentes de UI padronizados.
* **RxJS:** Gerenciamento de fluxo de dados.

---

## Segurança e Regras de Acesso (RBAC)

A aplicação segue uma arquitetura em camadas bem definida:
* **Controllers:** Exposição dos endpoints REST.
* **Services:** Regras de negócio.
* **Repositories (JDBC):** Camada de dados com SQL nativo, garantindo que não haja sobrecarga de frameworks ORM.
* **RBAC:** Controle de acesso baseado em Roles (`ADMIN` e `USER`). Todas as requisições (exceto login e registro) exigem o envio do token no cabeçalho `Authorization: Bearer <token>`.

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

## 🛠️ Tratamento de Exceções
O sistema utiliza um `RestControllerAdvice` para capturar exceções globalmente, garantindo que o cliente receba sempre um JSON estruturado e evitando o vazamento de detalhes internos da infraestrutura (Stack Traces).

---

## Testes Automatizados

O projeto conta com uma suíte de testes automatizados focada em garantir a qualidade e resiliência da aplicação. Os testes foram estruturados seguindo o padrão **AAA (Arrange, Act, Assert)**.

* **Testes Unitários (Regra de Negócio):** Utilizando **JUnit 5**, **Mockito** e **AssertJ**, as camadas de serviço (`ContatoService`, `GrupoService`, `EtiquetaService`) são testadas de forma isolada. Foram implementados Mocks (`@Mock`, `@InjectMocks`)  e o tratamento de exceções (ex: `EntityNotFoundException` para IDs inexistentes) funcionem corretamente sem a necessidade de instanciar o contexto do Spring ou acessar o banco de dados real.

## Como executar o projeto localmente

### Pré-requisitos
* Java 21 (JDK) e Node.js (v20+).
* PostgreSQL rodando localmente (porta 5432).

1. Clone o repositório para a sua máquina:
```bash
git clone <url-do-repositorio>
```

2. Abra o projeto na sua IDE de preferência (IntelliJ).

3. Configure as variáveis de ambiente necessárias no arquivo application.properties / ou no .env (ex: senha do banco, api.security.token.secret).

4. Execute a classe ExercicioSpringBootApplication.java.

5. A API estará disponível em http://localhost:8080.

### 6. Rodando o Frontend
Na pasta `frontend/contato`:

```bash
npm install
npx ng serve

```



