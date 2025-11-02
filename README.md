# 🍽️ Chef API - Usuários

> Sistema backend desenvolvido em **Spring Boot (DDD + Hexagonal)** para gestão de usuários de uma plataforma compartilhada de restaurantes.  
> O projeto é a **primeira fase** do sistema Chef, responsável por autenticação, cadastro e gerenciamento de usuários (clientes e donos de restaurante).

---

## 🚀 Tecnologias principais

- **Java 21**
- **Spring Boot 3.5**
- **Spring Data JPA / Hibernate**
- **PostgreSQL**
- **MapStruct**
- **Flyway** (migrações e seed)
- **OpenAPI 3 / Swagger UI**
- **Docker Compose**
- **Jakarta Validation**
- **Spring ProblemDetail (RFC 7807)**
- **Eventos de Domínio (Domain Events)**

---

## 📚 Arquitetura (DDD)

A aplicação segue o modelo de **Domain-Driven Design**, com separação clara de camadas:

```
application/
 ├── domain/                  # Entidades e regras de negócio puras
 ├── event/                   # Eventos de domínio
 ├── mapper/                  # Mapeadores entre domínios e DTOs
 ├── ports/
 │    ├── inbound/            # Casos de uso (input ports)
 │    └── outbound/           # Interfaces de persistência e serviços externos
 └── usecase/                 # Implementações dos casos de uso

infrastructure/
 ├── api/rest/                # Controllers (adapters REST)
 │    ├── generated/          # Código gerado via OpenAPI
 │    └── mapper/             # MapStruct entre DTOs e domínio
 ├── persistence/             # Adaptadores de repositório (JPA)
 ├── event/                   # Publicação de eventos
 └── config/                  # Configurações Spring, Flyway, etc.
```

---

## ⚙️ Funcionalidades

✅ **Cadastro, atualização e exclusão** de usuários  
✅ **Busca por nome** ou **ID**  
✅ **Validação de login** (`/api/v1/auth/login`)  
✅ **Troca de senha** com endpoint separado  
✅ **Atualização de dados** em endpoint distinto  
✅ **E-mail e login únicos** (validados no domínio e no banco)  
✅ **Registro automático da data da última alteração**  
✅ **Tratamento de erros padronizado (RFC 7807)**  
✅ **Versionamento de API** (`/api/v1/...`)  
✅ **Eventos de domínio:** `UsuarioCriado`  
✅ **Documentação com Swagger / OpenAPI**

---

## 🧩 Tipos de Usuário

- `CLIENTE`  
- `DONO_RESTAURANTE`  
- `ADMIN` (para gestão do sistema)

---

## 🧱 Modelagem de Banco

O sistema usa **PostgreSQL** com migrações via Flyway.

Migração inicial (`V1__create_tables.sql`):

```sql
CREATE TABLE usuarios (
    id UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    login VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    rua VARCHAR(255),
    numero VARCHAR(50),
    cidade VARCHAR(100),
    cep VARCHAR(20),
    uf VARCHAR(10),
    data_ultima_alteracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

## 🧰 Configuração do ambiente

### 1️⃣ Rodar com Docker

```bash
docker compose up --build
```

Banco e API subirão automaticamente.  
A API estará disponível em:  
👉 `http://localhost:8081/api/v1/swagger`

---

### 2️⃣ Rodar localmente (sem Docker)

Configure seu banco PostgreSQL local:

```
DB_NAME=userdb
DB_USER=postgres
DB_PASSWORD=postgres
DB_HOST=localhost
DB_PORT=5432
```

Edite o arquivo `application.yml` se necessário.

Execute:

```bash
./gradlew bootRun
```

ou com Maven:

```bash
mvn spring-boot:run
```

---

## 🧪 Testando via Postman

Uma coleção Postman (`postman-collection.json`) cobre os principais cenários:

| Categoria | Descrição |
|------------|------------|
| **Usuário** | Criação, atualização, busca e exclusão |
| **Senha** | Troca de senha (válida e inválida) |
| **Login** | Autenticação válida e inválida |
| **Erros** | E-mail/login duplicados, senhas curtas etc. |

### Endpoints principais

| Método | Endpoint | Descrição |
|--------|-----------|-----------|
| **POST** | `/api/v1/usuarios` | Cadastra novo usuário |
| **GET** | `/api/v1/usuarios/{id}` | Busca usuário por ID |
| **GET** | `/api/v1/usuarios?nome=` | Busca usuário por nome |
| **PUT** | `/api/v1/usuarios/{id}` | Atualiza dados do usuário |
| **PATCH** | `/api/v1/usuarios/{id}/senha` | Altera senha |
| **DELETE** | `/api/v1/usuarios/{id}` | Exclui usuário |
| **POST** | `/api/v1/auth/login` | Valida login e senha |

---

## 🧩 Exemplos de requisições

### Criar usuário
```bash
POST /api/v1/usuarios
```
```json
{
  "nome": "João Tavares",
  "email": "joao@example.com",
  "login": "joaotavares",
  "senha": "123456",
  "tipo": "DONO_RESTAURANTE",
  "endereco": {
    "rua": "Rua das Flores",
    "numero": "10",
    "cidade": "Barbacena",
    "cep": "36200-000",
    "uf": "MG"
  }
}
```

### Login
```bash
POST /api/v1/auth/login
```
```json
{
  "login": "joaotavares",
  "senha": "123456"
}
```

Resposta:
```json
{
  "id": "9f70a12b-81a4-4d82-9d6d-03e68bb76412",
  "nome": "João Tavares",
  "email": "joao@example.com",
  "tipo": "DONO_RESTAURANTE"
}
```

---

## 🧾 Erros padronizados (RFC 7807)

Todas as respostas de erro seguem o formato:

```json
{
  "type": "https://example.com/problems/bad-request",
  "title": "Bad Request",
  "status": 400,
  "detail": "Login já cadastrado: joaotavares",
  "instance": "/api/v1/usuarios"
}
```

---

## 🧪 Testes Unitários

- Casos de uso testados com JUnit 5 e Mockito.  
- Cobertura inclui:
  - `CadastrarUsuarioUseCase`
  - `AlterarSenhaUseCase`
  - `ValidarLoginUseCase`
  - `UsuarioRepositoryAdapter`

Execute:

```bash
./gradlew test
```

ou:
```bash
mvn test
```

---

## 🧠 Domínio e Eventos

- **Evento de domínio:** `UsuarioCriado`
- **Publisher:** `DomainEventPublisher`
- **Objetivo:** publicar eventos de criação para integração futura (ex.: envio de e-mail, auditoria etc.)

---

## 📘 Documentação Swagger

Acesse após iniciar a aplicação:

📄 **Swagger UI:**  
[http://localhost:8081/api/v1/swagger](http://localhost:8081/api/v1/swagger)

📘 **OpenAPI JSON:**  
[http://localhost:8081/api/v1/docs](http://localhost:8081/api/v1/docs)

---

## 🧱 Licença
Este projeto é de uso educacional, desenvolvido como parte de um estudo de **engenharia de software aplicada com DDD e Spring Boot**.
