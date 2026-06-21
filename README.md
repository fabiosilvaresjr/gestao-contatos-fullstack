# API RESTful - Gerenciamento de Contatos

Este projeto é uma API RESTful desenvolvida em Java com Spring Boot para o gerenciamento de contatos, permitindo a organização dos registros através de grupos e etiquetas. O sistema foi construído aplicando boas práticas de Engenharia de Software, com foco em arquitetura em camadas e performance no acesso aos dados.

## Arquitetura e Padrões Aplicados
O projeto segue a arquitetura dividida em camadas para garantir a separação de responsabilidades e fácil manutenção:
* **Controllers:** Exposição dos endpoints RESTful.
* **Services:** Isolamento e processamento das regras de negócio.
* **Repositories (SQL Nativo):** Em vez de utilizar ferramentas ORM automáticas (como Hibernate puro), o acesso aos dados foi construído com foco em previsibilidade e performance, utilizando consultas SQL escritas manualmente (JDBC/Native Queries).
* **DTOs (Data Transfer Objects):** Transferência segura de dados entre as camadas, garantindo validações rigorosas e evitando a exposição de dados sensíveis na resposta da API.

## Funcionalidades
* CRUD completo de **Contatos** (com validações de dados aplicadas).
* CRUD base de **Grupos**.
* CRUD base de **Etiquetas**.
* Relacionamento N:N (Muitos-para-Muitos) gerenciado de forma otimizada entre Contatos e Etiquetas.

## Tecnologias Utilizadas
* **Linguagem:** Java 26
* **Framework:** Spring Boot
* **Banco de Dados:** PostgreSQL
* **Persistência:** JDBC / Consultas Nativas (Native Queries)
* **Migrations:** Flyway (para versionamento estruturado do banco de dados)
* **Testes de API:** Postman

## Como executar o projeto

### 1. Pré-requisitos
* Ter o **Java 26** instalado na máquina.
* Ter o **PostgreSQL** instalado e rodando localmente (ou em um container Docker).

### 2. Configuração do Banco de Dados e Variáveis de Ambiente
Este projeto utiliza variáveis de ambiente para proteger credenciais sensíveis. 

Antes de rodar a aplicação, crie um banco de dados no seu PostgreSQL. Em seguida, na raiz do projeto, crie um arquivo chamado `.env` e preencha com as suas credenciais locais:

```env
DB_URL=jdbc:postgresql://localhost:5432/nome_do_seu_banco
DB_USER=seu_usuario
DB_PASSWORD=sua_senha
```

```O arquivo application.properties já está pré-configurado para consumir essas variáveis dinamicamente, garantindo a segurança do repositório:

spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
```

### 3. Executando a Aplicação
Como o projeto utiliza o Flyway, as tabelas serão criadas automaticamente na primeira execução através dos scripts de migration.

Para iniciar o servidor, basta executar o arquivo principal da aplicação:

src/main/java/com/example/exercicio_springboot/ExercicioSpringBootApplication.java

A API estará disponível para receber requisições (por padrão, na porta 8080).
