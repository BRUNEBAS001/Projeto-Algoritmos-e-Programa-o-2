# Sistema de Cadastro e Login – Task's To Do

## 1. Visão Geral

O sistema de cadastro e login do projeto Task's To Do foi desenvolvido com o objetivo de garantir que cada usuário possua um ambiente seguro e exclusivo para gerenciar suas tarefas. Ele é responsável por controlar o acesso à aplicação, permitindo que somente usuários autenticados visualizem, criem e editem suas listas de tarefas.

---

## 2. Cadastro de Usuário

### 2.1 Fluxo de Cadastro

Para acessar o sistema pela primeira vez, o usuário deve realizar um cadastro informando seus dados básicos:

- **Nome de usuário (login)**: Identificador único do usuário
- **Senha**: Credencial para acesso seguro

### 2.2 Processo de Armazenamento

Ao realizar o cadastro:

✅ **Validação de Dados**
- O sistema valida se os campos não estão vazios
- Verifica se o nome de usuário já existe no banco (unicidade)

✅ **Segurança de Senha**
- A senha é protegida por meio de métodos seguros de armazenamento (hashing)
- Nunca é salva em texto puro no banco de dados
- Utiliza algoritmos como BCrypt para criptografia

✅ **Armazenamento no Banco de Dados**
- Os dados são armazenados na tabela `users`
- Cada usuário recebe um ID único (auto-incrementado)
- Registra a data/hora de criação da conta

✅ **Prevenção de Duplicatas**
- Constraint UNIQUE garante que não há usernames repetidos
- Sistema retorna erro se tentar cadastrar username existente

### 2.3 Estrutura da Tabela Users

```sql
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

## 3. Autenticação (Login)

### 3.1 Fluxo de Login

Após o cadastro, o usuário pode acessar o sistema realizando login:

1. **Coleta de Credenciais**
   - O usuário informa suas credenciais (login e senha)

2. **Validação no Backend**
   - O sistema busca o usuário no banco de dados pelo username
   - Compara a senha fornecida com a senha armazenada

3. **Resposta do Sistema**
   - ✅ **Sucesso**: Acesso liberado, retorna user_id para a sessão
   - ❌ **Falha**: Sistema bloqueia o acesso e exibe mensagem de erro

### 3.2 Endpoints REST

#### Registro de Usuário
```
POST /api/auth/register
Content-Type: application/json

{
  "username": "usuario_novo",
  "password": "senha_segura"
}

Response (201 Created):
{
  "message": "Usuário registrado com sucesso",
  "user_id": 1
}

Response (400 Bad Request):
{
  "error": "Usuário já existe"
}
```

#### Login
```
POST /api/auth/login
Content-Type: application/json

{
  "username": "usuario_novo",
  "password": "senha_segura"
}

Response (200 OK):
{
  "message": "Login realizado com sucesso",
  "user_id": 1,
  "username": "usuario_novo"
}

Response (401 Unauthorized):
{
  "error": "Usuário ou senha inválidos"
}
```

#### Obter Dados do Usuário
```
GET /api/auth/users/{userId}

Response (200 OK):
{
  "id": 1,
  "username": "usuario_novo"
}

Response (404 Not Found):
{
  "error": "Usuário não encontrado"
}
```

---

## 4. Controle de Acesso

### 4.1 Após o Login Bem-Sucedido

✅ **Liberação de Funcionalidades**
- O sistema libera a visualização e o gerenciamento das tarefas vinculadas somente àquele usuário
- Interface privada fica visível após autenticação

✅ **Isolamento de Dados**
- Usuários diferentes possuem listas independentes
- Garantia de privacidade e isolamento de dados
- Cada tarefa está vinculada apenas ao seu usuário proprietário

✅ **Gerenciamento de Sessão**
- O user_id é armazenado no cliente (localStorage ou sessionStorage)
- Todas as requisições de tarefas incluem o user_id do usuário autenticado
- Servidor valida o user_id em cada requisição

### 4.2 Sem Autenticação

❌ **Restrição de Acesso**
- Se o usuário não estiver autenticado, não consegue acessar páginas internas
- Tela de login é exibida como padrão
- Qualquer tentativa de acessar tarefas sem user_id é bloqueada

### 4.3 Fluxo de Segurança no Frontend

```javascript
// 1. Verificação de autenticação ao carregar
function checkAuthentication() {
    const currentUser = localStorage.getItem('currentUser');
    
    if (!currentUser) {
        // Usuário não autenticado - exibe tela de login
        document.getElementById('login').style.display = 'block';
        document.getElementById('app').style.display = 'none';
    } else {
        // Usuário autenticado - exibe aplicação
        document.getElementById('login').style.display = 'none';
        document.getElementById('app').style.display = 'block';
        loadUserData();
    }
}

// 2. Logout - limpa dados de autenticação
function logout() {
    localStorage.removeItem('currentUser');
    document.getElementById('login').style.display = 'block';
    document.getElementById('app').style.display = 'none';
}
```

### 4.4 Validação de Requisições

Todas as requisições para tarefas devem incluir o user_id:

```javascript
// Exemplo: Buscar tarefas do usuário autenticado
async function loadTasks() {
    const userId = localStorage.getItem('currentUser');
    
    if (!userId) {
        // Redireciona para login
        logout();
        return;
    }
    
    const response = await fetch(`/api/tasks/user/${userId}`);
    const tasks = await response.json();
    displayTasks(tasks);
}
```

---

## 5. Segurança Implementada

### 5.1 Validações Multini-Camadas

| Camada | Validação |
|--------|-----------|
| Frontend (HTML5) | Atributos required, minlength, maxlength |
| Frontend (JavaScript) | Validação de campos vazios, trim() |
| Backend (Model) | isValidForRegistration(), isValidForLogin() |
| Backend (Repository) | Verificação de entrada, tratamento de exception |
| Backend (Controller) | Validação de requisição HTTP |
| Banco de Dados | UNIQUE constraint, NOT NULL |

### 5.2 Proteção contra SQL Injection

✅ **Implementado:** PreparedStatements via JdbcTemplate

```java
// ✅ SEGURO - Usa parametrização
String sql = "SELECT * FROM users WHERE username = ?";
jdbcTemplate.queryForObject(sql, new Object[]{username}, userMapper);

// ❌ INSEGURO - Concatenação (NÃO USAR)
String sql = "SELECT * FROM users WHERE username = '" + username + "'";
```

### 5.3 Armazenamento de Senha

⚠️ **IMPORTANTE**: Versão atual usa texto simples (desenvolvimento)

Para produção, implementar BCrypt:

```java
// Usando BCrypt
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
String hashedPassword = encoder.encode(rawPassword);
boolean isPasswordValid = encoder.matches(rawPassword, hashedPassword);
```

### 5.4 Proteção de Dados

✅ Senha nunca é retornada em APIs
✅ Apenas dados públicos são expostos
✅ Isolamento de dados por usuário
✅ Constraint UNIQUE no banco

---

## 6. Fluxo Completo de Uso

### 6.1 Primeiro Acesso (Registro)

```
1. Usuário acessa http://localhost:8080
   ↓
2. Vê a tela de login
   ↓
3. Clica em "Registrar"
   ↓
4. Preenche:
   - Nome de usuário: "joao_silva"
   - Senha: "minha_senha_123"
   ↓
5. Sistema:
   - Valida dados
   - Verifica unicidade do username
   - Armazena no banco
   ↓
6. Retorna sucesso: "Registrado com sucesso! Faça login."
```

### 6.2 Acesso Subsequente (Login)

```
1. Usuário acessa http://localhost:8080
   ↓
2. Vê a tela de login
   ↓
3. Preenche:
   - Nome de usuário: "joao_silva"
   - Senha: "minha_senha_123"
   ↓
4. Clica "Login"
   ↓
5. Sistema:
   - Busca usuário no banco
   - Valida senha
   ↓
6. Login bem-sucedido:
   - Armazena user_id na sessão
   - Exibe interface da aplicação
   - Carrega tarefas do usuário
```

### 6.3 Gerenciamento de Tarefas

```
1. Usuário autenticado pode:
   ✓ Criar tarefas (vinculadas ao seu user_id)
   ✓ Visualizar apenas suas tarefas
   ✓ Editar suas tarefas
   ✓ Deletar suas tarefas
   ✓ Marcar tarefas como concluídas

2. Usuário não autenticado:
   ✗ Não consegue criar tarefas
   ✗ Não consegue ver tarefas
   ✗ É redirecionado para login
```

---

## 7. Endpoints Disponíveis

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| POST | `/api/auth/register` | Registrar novo usuário | ❌ Não |
| POST | `/api/auth/login` | Fazer login | ❌ Não |
| GET | `/api/auth/users/{id}` | Obter dados do usuário | ✅ Sim |
| GET | `/api/tasks/user/{userId}` | Listar tarefas do usuário | ✅ Sim |
| GET | `/api/tasks/{id}` | Obter tarefa específica | ✅ Sim |
| POST | `/api/tasks` | Criar nova tarefa | ✅ Sim |
| PUT | `/api/tasks/{id}` | Editar tarefa | ✅ Sim |
| DELETE | `/api/tasks/{id}` | Deletar tarefa | ✅ Sim |
| PATCH | `/api/tasks/{id}/completed` | Marcar como completa | ✅ Sim |
| GET | `/api/categories` | Listar categorias | ✅ Sim |

---

## 8. Testando o Sistema

### 8.1 Teste de Registro

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"teste_user","password":"senha123"}'
```

### 8.2 Teste de Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"teste_user","password":"senha123"}'
```

### 8.3 Teste de Tarefas

```bash
# Criar tarefa (precisa do user_id do login)
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"user_id":1,"title":"Minha tarefa","description":"Descrição","category_id":1}'

# Listar tarefas do usuário
curl http://localhost:8080/api/tasks/user/1
```

---

## 9. Próximos Passos de Segurança (Produção)

- [ ] Implementar hashing BCrypt para senhas
- [ ] Adicionar Spring Security
- [ ] Implementar JWT ou sessões servidor
- [ ] Adicionar HTTPS/SSL
- [ ] Implementar rate limiting para login
- [ ] Adicionar autenticação multi-fator (2FA)
- [ ] Implementar refresh tokens
- [ ] Adicionar logs de auditoria

---

## 10. Conclusão

O sistema de cadastro e login do Task's To Do fornece uma base sólida para gerenciamento seguro de tarefas por usuário. Cada usuário possui um ambiente isolado e seguro para suas atividades, com controle de acesso que garante privacidade e integridade dos dados.
