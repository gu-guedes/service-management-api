# Service Management API

REST API para gerenciamento de serviços, clientes e pacientes, desenvolvida com Spring Boot e autenticação JWT.

---

## 🚀 Tecnologias

| Tecnologia | Versão |
|---|---|
| Java | 17 |
| Spring Boot | 4.0.2 |
| Spring Security | — |
| Spring Data JPA | — |
| PostgreSQL | — |
| Liquibase | — |
| JWT (jjwt) | 0.11.5 |
| Springdoc OpenAPI (Swagger) | 2.8.8 |
| Lombok | — |
| Maven | — |

---

## ⚙️ Configuração e execução

### Pré-requisitos

- Java 17+
- Maven 3.8+
- PostgreSQL rodando localmente

### 1. Configure o banco de dados

Crie um banco no PostgreSQL:

```sql
CREATE DATABASE service_management_db;
```

### 2. Ajuste as credenciais em `application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/service_management_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

### 3. Execute a aplicação

```bash
./mvnw spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

> O Liquibase executa as migrations automaticamente na inicialização.

---

## 🔐 Autenticação

A API utiliza **JWT (Bearer Token)**. Rotas protegidas exigem o header:

```
Authorization: Bearer <token>
```

### Obter token — `POST /auth/login`

```json
{
  "username": "seu_usuario",
  "password": "sua_senha"
}
```

### Registrar usuário — `POST /app-users` *(público)*

```json
{
  "username": "seu_usuario",
  "password": "sua_senha"
}
```

---

## 📋 Endpoints

### 👤 Usuários — `/app-users`

| Método | Rota | Auth | Descrição |
|---|---|---|---|
| `POST` | `/app-users` | ❌ | Cria novo usuário |
| `GET` | `/app-users` | ✅ | Lista todos os usuários |
| `GET` | `/app-users/{id}` | ✅ | Busca usuário por ID |
| `PUT` | `/app-users/{id}` | ✅ | Atualiza usuário |
| `DELETE` | `/app-users/{id}` | ✅ | Remove usuário |

### 🏢 Clientes — `/customers`

| Método | Rota | Auth | Descrição |
|---|---|---|---|
| `POST` | `/customers` | ✅ | Cria novo cliente |
| `GET` | `/customers` | ✅ | Lista todos os clientes |
| `GET` | `/customers/{id}` | ✅ | Busca cliente por ID |
| `PUT` | `/customers/{id}` | ✅ | Atualiza cliente |
| `DELETE` | `/customers/{id}` | ✅ | Remove cliente |

### 🏥 Pacientes — `/patients`

| Método | Rota | Auth | Descrição |
|---|---|---|---|
| `POST` | `/patients` | ❌ | Cria novo paciente |
| `GET` | `/patients` | ❌ | Lista todos os pacientes |
| `GET` | `/patients/{id}` | ❌ | Busca paciente por ID |
| `PUT` | `/patients/{id}` | ❌ | Atualiza paciente |
| `DELETE` | `/patients/{id}` | ❌ | Remove paciente |

---

## 📖 Documentação interativa (Swagger)

Com a aplicação rodando, acesse:

```
http://localhost:8080/swagger-ui.html
```

Clique em **Authorize** e informe o Bearer Token para testar as rotas protegidas.

---

## 🗄️ Migrations

As migrations do banco de dados são gerenciadas pelo **Liquibase** e ficam em:

```
src/main/resources/db/changelog/db.changelog-master.sql
```

---

## 🌐 CORS

A API está configurada para aceitar requisições do frontend Angular em:

```
http://localhost:4200
```
