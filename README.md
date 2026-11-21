# Gestão de Contatos

Aplicação full stack para gerenciar contatos organizados por grupos e etiquetas, com autenticação JWT e controle de acesso por perfil (RBAC).

Projeto desenvolvido durante o programa de estágio, como exercício para consolidar Spring Boot, Spring Security, Flyway, testes automatizados e Angular.

## Stack

**Back-end (API REST)**
- Java e Spring Boot
- Spring Security com JWT (Auth0 java-jwt)
- Spring Data JPA e Hibernate, com consultas JPQL que projetam direto para DTOs
- PostgreSQL com migrations versionadas pelo Flyway
- Lombok
- JUnit 5, Mockito e AssertJ

**Front-end (SPA)**
- Angular
- Angular Material
- RxJS

## Arquitetura

Arquitetura em camadas: `controller` (endpoints REST), `service` (regras de negócio), `repository` (acesso a dados), `entity` e `dto`.

## Segurança e perfis de acesso

Todas as rotas, exceto login e registro, exigem o header `Authorization: Bearer <token>`.

| Recurso | Ação | Permissão |
| :--- | :--- | :--- |
| Autenticação | `POST /auth/login` e `POST /auth/register` | Público |
| Contatos, grupos e etiquetas | `GET` (listar e buscar) | `USER` ou `ADMIN` |
| Contatos, grupos e etiquetas | `POST`, `PUT`, `PATCH`, `DELETE` | Apenas `ADMIN` |

## Tratamento global de exceções

Um `@RestControllerAdvice` centraliza os erros e devolve respostas JSON padronizadas, sem expor stack traces:

- **400:** falha de validação (`@Valid`), com a lista de campos e mensagens.
- **403:** acesso negado quando um `USER` tenta uma rota exclusiva de `ADMIN`.
- **404:** recurso inexistente (`EntityNotFoundException`).
- **500:** erro interno genérico, com mensagem amigável.

## Testes

- **Unitários:** `ContatoService`, `GrupoService` e `EtiquetaService`, isolados com Mockito, seguindo o padrão AAA (Arrange, Act, Assert).
- **Integração:** `ContatoControllerIT`, com `@SpringBootTest` subindo a aplicação em porta aleatória.

```bash
cd Exercicio_SpringBoot
./mvnw test
```

## Como rodar

Pré-requisitos: Java 21, Node.js 20+ e PostgreSQL local (porta 5432).

1. Clone o repositório:

```bash
git clone https://github.com/fabiosilvaresjr/gestao-contatos-fullstack.git
```

2. Defina as variáveis de ambiente do back-end: `DB_URL`, `DB_USER`, `DB_PASSWORD` e `JWT_SECRET`.

3. Suba a API (a partir de `Exercicio_SpringBoot/`):

```bash
./mvnw spring-boot:run
```

A API fica em `http://localhost:8080`. O Flyway cria as tabelas na primeira execução.

4. Suba o front-end (a partir de `frontend/contato/`):

```bash
npm install
npx ng serve
```
