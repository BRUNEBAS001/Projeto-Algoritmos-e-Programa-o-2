# Task's To Do - Sistema de Gerenciamento de Tarefas

## 📋 Visão Geral

**Task's To Do** é uma aplicação web completa para gerenciamento de tarefas com autenticação segura, desenvolvida com **Spring Boot 3.2.3** no backend e **HTML5/CSS3/JavaScript** no frontend.

---

## 🎯 Funcionalidades Principais

### 1. Sistema de Autenticação Seguro
- ✅ Registro de novo usuário
- ✅ Login com validação de credenciais
- ✅ Proteção contra SQL Injection (PreparedStatements)
- ✅ Validação de entrada em múltiplos níveis
- ✅ Constraint de unicidade (username único)
- ✅ Comprimento mínimo/máximo de password e username

### 2. Gerenciamento de Tarefas
- ✅ Criar novas tarefas
- ✅ Listar tarefas por usuário
- ✅ Editar tarefas existentes
- ✅ Deletar tarefas
- ✅ Marcar tarefas como concluídas
- ✅ Filtrar por categoria

### 3. Categorização
- ✅ 4 categorias pré-definidas:
  - Estudo
  - Trabalho
  - Pessoal
  - Urgente
- ✅ Associar tarefas a categorias
- ✅ Visualizar tarefas por categoria

### 4. Interface Amigável
- ✅ Tema escuro profissional
- ✅ Responsivo (mobile-friendly)
- ✅ Interfaceintuitiva
- ✅ Feedback visual de ações
- ✅ Suporte a datas de vencimento

---

## 🏗️ Arquitetura

### Backend Stack
```
Spring Boot 3.2.3
├── Spring Web (REST APIs)
├── Spring Data JDBC (Acesso ao BD)
├── Tomcat Embedded (Servidor)
└── SQLite (Banco de Dados)
```

### Frontend Stack
```
HTML5 / CSS3 / JavaScript
├── JavaScript Vanilla (sem frameworks)
├── Fetch API (Requisições HTTP)
├── DOM Manipulation
└── LocalStorage (Sessão)
```

### Banco de Dados
```
SQLite (Relacional)
├── users (Autenticação)
├── tasks (Gerenciamento de tarefas)
└── categories (Categorização)
```

---

## 📁 Estrutura do Projeto

```
tasks-to-do-springboot/
├── src/
│   └── main/
│       ├── java/com/example/taskstodo/
│       │   ├── Application.java              # Classe principal Spring Boot
│       │   ├── model/
│       │   │   ├── User.java                # Entidade usuário com validações
│       │   │   ├── Task.java                # Entidade tarefa
│       │   │   └── Category.java            # Entidade categoria
│       │   ├── repository/
│       │   │   ├── UserRepository.java      # CRUD de usuários (JDBC)
│       │   │   ├── TaskRepository.java      # CRUD de tarefas (JDBC)
│       │   │   └── CategoryRepository.java  # CRUD de categorias (JDBC)
│       │   ├── controller/
│       │   │   ├── AuthController.java      # Endpoints /api/auth
│       │   │   ├── TaskController.java      # Endpoints /api/tasks
│       │   │   └── CategoryController.java  # Endpoints /api/categories
│       │   └── config/
│       │       └── StartupConfig.java       # Inicialização de tabelas
│       └── resources/
│           ├── application.properties       # Configuração Spring Boot
│           ├── static/
│           │   ├── index.html              # Frontend HTML
│           │   ├── app.js                  # Lógica JavaScript
│           │   └── app.css                 # Estilos CSS
│           └── database/
│               └── tasks.db                # Banco SQLite (auto-criado)
├── pom.xml                                 # Dependências Maven
├── README.md                               # Este arquivo
├── AUTENTICACAO.md                         # Guia de autenticação
├── TESTES_AUTENTICACAO.md                  # Guia de testes
├── SISTEMA_SEGURO.md                       # Documentação de segurança
└── start.bat                               # Script para iniciar a aplicação
```

---

## 🚀 Quick Start

### 1. Pré-requisitos
- Java 17 ou superior
- Maven 3.9+
- Windows / Linux / macOS

### 2. Clone/Acesse o Projeto
```bash
cd c:\Users\DESKTOP\Downloads\tasks-to-do-springboot
```

### 3. Compile o Projeto
```bash
mvn clean compile -DskipTests
```

### 4. Empacote (opcional, para JAR)
```bash
mvn clean package -DskipTests
```

### 5. Execute a Aplicação

#### Opção A: Usando Maven
```bash
mvn spring-boot:run
```

#### Opção B: Usando JAR
```bash
java -jar target/tasks-to-do-1.0.0.jar
```

#### Opção C: Usando Script (Windows)
```bash
.\start.bat
```

### 6. Acesse a Aplicação
Abra o navegador e acesse:
```
http://localhost:8080
```

---

## 📚 Documentação Completa

### 1. Autenticação
Para entender como funciona o sistema de login/registro:
```
→ Veja: AUTENTICACAO.md
```

### 2. Testes
Para testar todos os endpoints da API:
```
→ Veja: TESTES_AUTENTICACAO.md
```

### 3. Segurança
Para detalhes sobre proteção e validações:
```
→ Veja: SISTEMA_SEGURO.md
```

---

## 🔐 Segurança Implementada

### ✅ Proteção contra SQL Injection
- Todas as queries usam **PreparedStatements**
- Parâmetros são passados separadamente da SQL
- Impossível concatenar valores na query

### ✅ Validação de Input
- Username: 3-50 caracteres
- Password: 6-100 caracteres
- Campos obrigatórios validados
- Verificação de duplicidade no banco

### ✅ Proteção de Dados
- Senhas nunca são retornadas em APIs
- Apenas dados públicos são expostos
- Constraint UNIQUE protege username
- Tratamento de erro genérico (não expõe detalhes)

### ✅ HTTP Status Codes
- 201: Registro bem-sucedido
- 200: Requisição bem-sucedida
- 400: Erro de validação
- 401: Credenciais inválidas
- 404: Recurso não encontrado
- 409: Conflito (username duplicado)

---

## 🔌 API REST Endpoints

### Autenticação

#### Registrar Novo Usuário
```
POST /api/auth/register
Content-Type: application/json

{
  "username": "novo_usuario",
  "password": "senha_segura"
}

Response (201):
{
  "message": "Usuário registrado com sucesso! Faça login.",
  "user_id": 1,
  "username": "novo_usuario"
}
```

#### Fazer Login
```
POST /api/auth/login
Content-Type: application/json

{
  "username": "novo_usuario",
  "password": "senha_segura"
}

Response (200):
{
  "message": "Login realizado com sucesso",
  "user_id": 1,
  "username": "novo_usuario"
}
```

#### Obter Dados do Usuário
```
GET /api/auth/users/{id}

Response (200):
{
  "id": 1,
  "username": "novo_usuario"
}
```

### Tarefas

#### Listar Tarefas do Usuário
```
GET /api/tasks/user/{userId}

Response (200):
[
  {
    "id": 1,
    "title": "Estudar Spring Boot",
    "description": "Implementar API REST",
    "categoryId": 1,
    "userId": 1,
    "dueDate": "2025-12-31",
    "completed": 0
  }
]
```

#### Criar Nova Tarefa
```
POST /api/tasks
Content-Type: application/json

{
  "title": "Minha tarefa",
  "description": "Descrição da tarefa",
  "category_id": 1,
  "user_id": 1,
  "due_date": "2025-12-31",
  "completed": 0
}

Response (201):
{
  "id": 1,
  "title": "Minha tarefa",
  ...
}
```

#### Editar Tarefa
```
PUT /api/tasks/{id}
Content-Type: application/json

{
  "title": "Tarefa atualizada",
  "description": "Nova descrição",
  "category_id": 1,
  "completed": 0
}

Response (200): OK
```

#### Deletar Tarefa
```
DELETE /api/tasks/{id}

Response (204): No Content
```

#### Marcar Tarefa como Concluída
```
PATCH /api/tasks/{id}/completed
Content-Type: application/json

{
  "completed": 1
}

Response (200): OK
```

### Categorias

#### Listar Todas as Categorias
```
GET /api/categories

Response (200):
[
  { "id": 1, "name": "Estudo" },
  { "id": 2, "name": "Trabalho" },
  { "id": 3, "name": "Pessoal" },
  { "id": 4, "name": "Urgente" }
]
```

---

## 🧪 Testes

### Testar Registro
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"teste","password":"senha123"}'
```

### Testar Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"teste","password":"senha123"}'
```

### Testar Listar Tarefas
```bash
curl -X GET http://localhost:8080/api/tasks/user/1 \
  -H "Content-Type: application/json"
```

Veja **TESTES_AUTENTICACAO.md** para testes completos!

---

## 💾 Banco de Dados

### Tabelas Criadas Automaticamente

#### users
```sql
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    created_at TEXT DEFAULT (datetime('now'))
);
```

#### tasks
```sql
CREATE TABLE tasks (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    description TEXT,
    category_id INTEGER,
    user_id INTEGER NOT NULL,
    due_date TEXT,
    completed INTEGER DEFAULT 0,
    created_at TEXT DEFAULT (datetime('now')),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

#### categories
```sql
CREATE TABLE categories (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL UNIQUE
);
```

---

## 🎨 Interface Gráfica

### Temas
- ✅ Tema Escuro (padrão)
- ✅ Responsivo (mobile-friendly)
- ✅ Cores: Azul/Preto/Branco

### Componentes
- Formulário de Login/Registro
- Listagem de Tarefas
- Formulário de Criação/Edição de Tarefas
- Seletor de Categorias
- Data Picker para Datas de Vencimento
- Botões de Ação (Editar/Deletar/Concluir)

---

## 🔧 Configuração

### application.properties
```properties
# Servidor
server.port=8080

# Banco de Dados
spring.datasource.url=jdbc:sqlite:src/main/resources/database/tasks.db
spring.datasource.driver-class-name=org.sqlite.JDBC

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## 🚨 Troubleshooting

### Erro: "Port 8080 already in use"
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -i :8080
kill -9 <PID>
```

### Erro: "No POM in this directory"
```bash
# Certifique-se de estar no diretório raiz do projeto
cd tasks-to-do-springboot
mvn clean compile
```

### Banco de Dados Corrompido
```bash
# Deleta o banco (será recriado automaticamente)
rm src/main/resources/database/tasks.db
```

---

## 📈 Roadmap

### v1.0 ✅ (Atual)
- [x] Autenticação com validação
- [x] CRUD de tarefas
- [x] Categorização
- [x] Interface básica
- [x] Proteção contra SQL Injection

### v1.1 (Planejado)
- [ ] BCrypt para password hashing
- [ ] Rate limiting em login
- [ ] HTTPS/SSL

### v2.0 (Planejado)
- [ ] JWT tokens
- [ ] Refresh tokens
- [ ] Logging de auditoria
- [ ] 2FA (Two-Factor Authentication)

### v3.0 (Futuro)
- [ ] OAuth2 integration
- [ ] Single Sign-On (SSO)
- [ ] Mobile app
- [ ] Dark/Light theme toggle

---

## 📝 Licença

Este projeto é fornecido como está para fins educacionais.

---

## 👥 Contribuições

Bem-vindo aos reports de bug e melhorias!

---

## 📞 Suporte

Para dúvidas sobre o sistema:
1. Veja **AUTENTICACAO.md** - Funcionalidades
2. Veja **TESTES_AUTENTICACAO.md** - Como testar
3. Veja **SISTEMA_SEGURO.md** - Detalhes técnicos

---

## ✅ Checklist de Funcionalidades

- [x] Registro de usuário com validação
- [x] Login com autenticação
- [x] Criação de tarefas
- [x] Listagem de tarefas por usuário
- [x] Edição de tarefas
- [x] Deleção de tarefas
- [x] Marcação de tarefas como concluídas
- [x] Categorização
- [x] Interface responsiva
- [x] Tema escuro
- [x] Proteção contra SQL Injection
- [x] Validação de entrada
- [x] HTTP Status codes apropriados
- [x] Documentação completa
- [x] Testes de API

---

## 🎉 Conclusão

**Task's To Do** é um sistema completo e seguro de gerenciamento de tarefas com autenticação robusta e interface amigável.

**Status:** ✅ Pronto para uso

Aproveite! 🚀
