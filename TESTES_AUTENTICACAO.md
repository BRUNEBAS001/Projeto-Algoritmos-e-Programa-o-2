# Guia de Testes - Sistema de Autenticação Task's To Do

## Visão Geral

Este documento fornece testes práticos para validar o sistema de cadastro e login do Task's To Do com todas as melhorias de segurança implementadas.

---

## 1. Teste de Registro (Sign Up)

### 1.1 Teste de Sucesso - Registro Válido

**Endpoint:** `POST /api/auth/register`

**Request:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "joao_silva",
    "password": "senha_segura_123"
  }'
```

**Response Esperado (201 Created):**
```json
{
  "message": "Usuário registrado com sucesso! Faça login.",
  "user_id": 1,
  "username": "joao_silva"
}
```

✅ **Validações Passar:**
- Status HTTP: 201
- User ID é retornado
- Username é confirmado

---

### 1.2 Teste de Erro - Username Vazio

**Request:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "",
    "password": "senha_123"
  }'
```

**Response Esperado (400 Bad Request):**
```json
{
  "error": "Nome de usuário é obrigatório"
}
```

✅ **Validação:** Sistema rejeita username vazio

---

### 1.3 Teste de Erro - Password Vazia

**Request:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "novo_usuario",
    "password": ""
  }'
```

**Response Esperado (400 Bad Request):**
```json
{
  "error": "Senha é obrigatória"
}
```

✅ **Validação:** Sistema rejeita password vazia

---

### 1.4 Teste de Erro - Username Muito Curto

**Request:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "ab",
    "password": "senha_123"
  }'
```

**Response Esperado (400 Bad Request):**
```json
{
  "error": "Nome de usuário deve ter entre 3 e 50 caracteres"
}
```

✅ **Validação:** Username mínimo de 3 caracteres é enforçado

---

### 1.5 Teste de Erro - Password Muito Curta

**Request:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "novo_usuario",
    "password": "12345"
  }'
```

**Response Esperado (400 Bad Request):**
```json
{
  "error": "Senha deve ter entre 6 e 100 caracteres"
}
```

✅ **Validação:** Senha mínima de 6 caracteres é enforçada

---

### 1.6 Teste de Erro - Username Duplicado

**Request 1 (Sucesso):**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "usuario_duplicado",
    "password": "senha_123"
  }'
```

**Request 2 (Tentativa de Duplicação):**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "usuario_duplicado",
    "password": "outra_senha"
  }'
```

**Response Esperado (409 Conflict):**
```json
{
  "error": "Este nome de usuário já está em uso"
}
```

✅ **Validação:** Username deve ser único

---

## 2. Teste de Login (Sign In)

### 2.1 Teste de Sucesso - Login Válido

**Prerequisites:** Usuário "joao_silva" registrado com senha "senha_segura_123"

**Endpoint:** `POST /api/auth/login`

**Request:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "joao_silva",
    "password": "senha_segura_123"
  }'
```

**Response Esperado (200 OK):**
```json
{
  "message": "Login realizado com sucesso",
  "user_id": 1,
  "username": "joao_silva"
}
```

✅ **Validações Passar:**
- Status HTTP: 200
- User ID é retornado
- Username é confirmado
- Message de sucesso

---

### 2.2 Teste de Erro - Username Inválido

**Request:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "usuario_inexistente",
    "password": "qualquer_senha"
  }'
```

**Response Esperado (401 Unauthorized):**
```json
{
  "error": "Nome de usuário ou senha inválidos"
}
```

✅ **Validação:** Rejeita username não registrado

---

### 2.3 Teste de Erro - Senha Incorreta

**Request:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "joao_silva",
    "password": "senha_errada"
  }'
```

**Response Esperado (401 Unauthorized):**
```json
{
  "error": "Nome de usuário ou senha inválidos"
}
```

✅ **Validação:** Rejeita senha incorreta

---

### 2.4 Teste de Erro - Username Vazio

**Request:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "",
    "password": "senha_segura_123"
  }'
```

**Response Esperado (400 Bad Request):**
```json
{
  "error": "Nome de usuário é obrigatório"
}
```

✅ **Validação:** Rejeita username vazio

---

### 2.5 Teste de Erro - Password Vazia

**Request:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "joao_silva",
    "password": ""
  }'
```

**Response Esperado (400 Bad Request):**
```json
{
  "error": "Senha é obrigatória"
}
```

✅ **Validação:** Rejeita password vazia

---

## 3. Teste de Obtenção de Dados do Usuário

### 3.1 Teste de Sucesso - Obter Dados do Usuário

**Endpoint:** `GET /api/auth/users/{id}`

**Prerequisites:** Usuário com ID 1 registrado

**Request:**
```bash
curl -X GET http://localhost:8080/api/auth/users/1 \
  -H "Content-Type: application/json"
```

**Response Esperado (200 OK):**
```json
{
  "id": 1,
  "username": "joao_silva"
}
```

✅ **Validações Passar:**
- Status HTTP: 200
- ID do usuário é retornado
- Username é retornado
- **Importante:** Senha NOT é incluída na resposta

---

### 3.2 Teste de Erro - Usuário Não Encontrado

**Request:**
```bash
curl -X GET http://localhost:8080/api/auth/users/999 \
  -H "Content-Type: application/json"
```

**Response Esperado (404 Not Found):**
```json
{
  "error": "Usuário não encontrado"
}
```

✅ **Validação:** ID inválido retorna 404

---

## 4. Teste de Integração Completa - Fluxo de Usuário

### 4.1 Cenário Completo: Novo Usuário

**Passo 1: Registrar Novo Usuário**
```bash
# POST /api/auth/register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "maria_santos",
    "password": "minha_senha_456"
  }'
# Response: Status 201, user_id = 2
```

**Passo 2: Fazer Login**
```bash
# POST /api/auth/login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "maria_santos",
    "password": "minha_senha_456"
  }'
# Response: Status 200, user_id = 2
```

**Passo 3: Obter Dados do Usuário**
```bash
# GET /api/auth/users/2
curl -X GET http://localhost:8080/api/auth/users/2 \
  -H "Content-Type: application/json"
# Response: Status 200, username = "maria_santos"
```

✅ **Fluxo Completo:** Registro → Login → Obter Dados

---

## 5. Testes de Segurança

### 5.1 Teste de Proteção contra SQL Injection

**Request (Tentativa de SQL Injection):**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin\" OR \"1\"=\"1",
    "password": "qualquer_coisa"
  }'
```

**Response Esperado (401 Unauthorized):**
```json
{
  "error": "Nome de usuário ou senha inválidos"
}
```

✅ **Validação:** PreparedStatements protegem contra SQL Injection

---

### 5.2 Teste de Proteção de Dados - Password Não é Exposta

**Request:**
```bash
curl -X GET http://localhost:8080/api/auth/users/1 \
  -H "Content-Type: application/json"
```

**Response:**
```json
{
  "id": 1,
  "username": "joao_silva"
  // Password NÃO é incluído na resposta
}
```

✅ **Validação:** Senha nunca é retornada ao cliente

---

## 6. Cenários de Erro - Teste de Robustez

### 6.1 Teste com Payload Inválido

**Request (JSON inválido):**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d 'json_inválido'
```

**Response Esperado:** Erro 400 ou 500 (graceful error handling)

---

### 6.2 Teste com Headers Faltando

**Request (sem Content-Type):**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -d '{
    "username": "teste",
    "password": "senha123"
  }'
```

**Response Esperado:** Sistema continua funcionando

---

## 7. Matriz de Testes

| Teste | Endpoint | Método | Input Válido | Status Esperado | Descrição |
|-------|----------|--------|--------------|-----------------|-----------|
| Registro Sucesso | /api/auth/register | POST | username + password válidos | 201 | Cria novo usuário |
| Registro - Username Vazio | /api/auth/register | POST | username = "" | 400 | Valida campo obrigatório |
| Registro - Password Vazia | /api/auth/register | POST | password = "" | 400 | Valida campo obrigatório |
| Registro - Username Curto | /api/auth/register | POST | username = "ab" | 400 | Enforce tamanho mínimo |
| Registro - Password Curta | /api/auth/register | POST | password = "12345" | 400 | Enforce tamanho mínimo |
| Registro - Username Duplicado | /api/auth/register | POST | username existente | 409 | Constraint UNIQUE |
| Login Sucesso | /api/auth/login | POST | credenciais corretas | 200 | Autentica usuário |
| Login - Username Inválido | /api/auth/login | POST | username inexistente | 401 | Rejeita credencial inválida |
| Login - Password Incorreta | /api/auth/login | POST | password errada | 401 | Rejeita credencial inválida |
| Login - Username Vazio | /api/auth/login | POST | username = "" | 400 | Valida campo obrigatório |
| Login - Password Vazia | /api/auth/login | POST | password = "" | 400 | Valida campo obrigatório |
| Obter Usuário | /api/auth/users/{id} | GET | ID válido | 200 | Retorna dados públicos |
| Obter Usuário - Não Encontrado | /api/auth/users/{id} | GET | ID inválido | 404 | Usuário não existe |

---

## 8. Checkpoints de Segurança

- [x] Username e password são obrigatórios
- [x] Comprimento mínimo de username (3 caracteres)
- [x] Comprimento máximo de username (50 caracteres)
- [x] Comprimento mínimo de password (6 caracteres)
- [x] Comprimento máximo de password (100 caracteres)
- [x] Username deve ser único (constraint UNIQUE)
- [x] PreparedStatements protegem contra SQL Injection
- [x] Senha nunca é retornada ao cliente
- [x] Tratamento de erro genérico para credenciais inválidas
- [x] HTTP Status codes apropriados (201, 200, 400, 401, 404, 409)

---

## 9. Como Executar os Testes

### Opção 1: Usar cURL (Windows PowerShell)

```powershell
# Copie e cole os comandos curl nos exemplos acima no PowerShell
Invoke-WebRequest -Uri http://localhost:8080/api/auth/register `
  -Method POST `
  -Headers @{'Content-Type'='application/json'} `
  -Body '{"username":"teste","password":"senha123"}'
```

### Opção 2: Usar Postman

1. Abra Postman
2. Configure os endpoints com as informações acima
3. Execute os testes

### Opção 3: Usar Bruno

1. Crie coleção de requisições
2. Importe os exemplos acima
3. Execute sequencialmente

---

## 10. Conclusão

O sistema de autenticação do Task's To Do implementa:

✅ Validação robusta de entrada
✅ Proteção contra SQL Injection
✅ HTTP Status codes apropriados
✅ Tratamento de erro seguro
✅ Proteção de dados sensíveis
✅ Constraint de unicidade
✅ Limites de comprimento

**Próximas melhorias sugeridas:**
- [ ] Implementar BCrypt para hashing de password
- [ ] Adicionar rate limiting
- [ ] Implementar logging de auditoria
- [ ] Adicionar HTTPS
- [ ] Implementar JWT tokens
