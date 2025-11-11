# 🎯 Task's To Do — Guia de Uso das Novas Páginas

## 📱 Interface Separada em 3 Páginas

Você agora tem uma experiência muito mais limpa e organizada com páginas dedicadas:

### 1️⃣ **Página de Login** (`/login.html`)
Tela inicial para usuários que já têm cadastro.

**Elementos:**
- 📝 Campo de **Nome de Usuário**
- 🔑 Campo de **Senha**
- ☑️ Opção **"Lembrar-me neste computador"**
- 🔓 Botão **"Entrar"**
- 🔗 Link para criar nova conta

**O que acontece:**
- Você entra suas credenciais
- Sistema valida no backend (`POST /api/auth/login`)
- Se correto → Redirecionado para dashboard de tarefas
- Se errado → Mensagem de erro em vermelho

---

### 2️⃣ **Página de Cadastro** (`/register.html`)
Tela para criar nova conta no sistema.

**Elementos:**
- 📝 Campo de **Nome de Usuário** (3-50 caracteres)
- 🔑 Campo de **Senha** (6-100 caracteres)
  - Indicador visual de força: 🔴 Fraca → 🟢 Forte
- 🔐 Campo de **Confirmação de Senha**
- 📝 Botão **"Registrar"**
- 🔗 Link para voltar ao login

**O que acontece:**
- Você cria um novo usuário
- Sistema valida a força da senha
- Confirma se as senhas combinam
- Se sucesso → Redirecionado para login.html
- Se erro → Mensagem descrevendo o problema

---

### 3️⃣ **Dashboard de Tarefas** (`/index.html`)
Área principal para gerenciar suas tarefas.

**Elementos:**
- 👤 Seu ID de usuário no canto superior
- 🚪 Botão **"Sair"** para fazer logout
- ✏️ Formulário para criar nova tarefa:
  - Título (obrigatório)
  - Descrição (opcional)
  - Categoria (dropdown)
  - Data de vencimento
- ➕ Botão **"Adicionar Tarefa"**
- 📋 Lista de suas tarefas com opções de editar/excluir
- ✅ Checkbox para marcar tarefas como concluídas

---

## 🔐 Fluxo de Segurança

```
┌─────────────────────────────────────────────────────┐
│         ACESSO À APLICAÇÃO                         │
└─────────────────────────────────────────────────────┘
                        ↓
              ┌─ Verificar localStorage
              │
        ┌─────┴─────┐
        │           │
    user_id?    user_id?
      SIM         NÃO
       │           │
       ↓           ↓
   ✅ Abrir    ❌ Redirecionar
   Tarefas    para /login.html
```

---

## 💾 Dados Armazenados (localStorage)

```javascript
// Após fazer login com sucesso:
localStorage.currentUser = "123"  // ID do usuário

// Se marcou "Lembrar-me":
localStorage.rememberUsername = "meu_usuario"
```

**Segurança:**
- ⚠️ localStorage NÃO armazena senha
- ⚠️ Apenas o ID do usuário é guardado
- ✅ A senha fica apenas na request ao servidor

---

## 🧪 Teste Rápido — Novo Usuário

**Passo 1:** Abra http://localhost:8080

**Passo 2:** Clique em "Criar nova conta"

**Passo 3:** Preencha:
```
Nome de Usuário: teste_user
Senha: Minha@Senha123
Confirmação: Minha@Senha123
```

**Passo 4:** Clique em "📝 Registrar"

**Passo 5:** Você será redirecionado para login.html

**Passo 6:** Faça login com as mesmas credenciais

**Passo 7:** ✅ Dashboard de tarefas deve aparecer!

---

## 🧪 Teste Rápido — Usuário Existente

Se você já criou um usuário antes:

**Passo 1:** Vá para http://localhost:8080/login.html

**Passo 2:** Preencha:
```
Nome de Usuário: seu_usuario
Senha: sua_senha
```

**Passo 3:** Opcionalmente, marque "Lembrar-me neste computador"

**Passo 4:** Clique em "🔓 Entrar"

**Passo 5:** ✅ Dashboard de tarefas!

---

## 🚪 Fazer Logout

Quando terminar de usar:

**Passo 1:** Clique no botão **"🚪 Sair"** no canto superior direito

**Passo 2:** Confirme a ação

**Passo 3:** localStorage.currentUser é removido

**Passo 4:** Você volta para /login.html

---

## ❌ Problemas Comuns

### "Impossível conectar em localhost:8080"
- ✅ Verifique se a aplicação Java está rodando
- ✅ Verifique se a porta 8080 está liberada
- ✅ Aguarde 30 segundos após iniciar

### "Usuário ou senha incorretos"
- ✅ Verifique se o usuário existe (tente registrar novo)
- ✅ Verifique a capitalização (senha é case-sensitive)
- ✅ Sem espaços antes/depois dos campos

### "Campos obrigatórios"
- ✅ Todos os campos têm requisitos mínimos:
  - Username: 3-50 caracteres
  - Senha: 6-100 caracteres
  - Confirm Senha: deve ser igual à senha

### "Redirecionamento para login infinito"
- ✅ localStorage.currentUser não está definido
- ✅ Limpe o localStorage do navegador (F12 → Application → Clear)
- ✅ Faça login novamente

---

## 🎨 Design das Páginas

Todas as 3 páginas seguem o mesmo tema visual:

**Cores:**
- 🟢 Verde: #0f9 (ações principais, sucesso)
- ⚫ Fundo: Gradiente de tons escuros (dark theme)
- ⚪ Texto: Branco/Cinza (alto contraste)
- 🔴 Erro: Vermelho suave (mensagens de erro)

**Interações:**
- 🔘 Botões com hover effeito (brilho verde)
- 📝 Campos com focus effect (border brilhante)
- ✨ Transições suaves de 0.3s
- 📱 Design responsivo para mobile

---

## 📚 Estrutura do Projeto

```
src/main/resources/static/
├── register.html ........... Página de cadastro
├── login.html .............. Página de login  ← NOVO
├── index.html .............. Dashboard (ATUALIZADO)
├── app.js .................. Lógica de tarefas (ATUALIZADO)
└── app.css ................. Estilos globais

API Endpoints:
├── POST /api/auth/register ... Criar novo usuário
├── POST /api/auth/login ....... Autenticar usuário
├── GET  /api/tasks/user/{id} . Listar tarefas
├── POST /api/tasks ........... Criar tarefa
├── PUT  /api/tasks/{id} ...... Atualizar tarefa
└── DELETE /api/tasks/{id} .... Deletar tarefa
```

---

## ✅ Checklist de Funcionalidade

- ✅ Página de login com formulário simples
- ✅ Página de registro com validação de senha
- ✅ Dashboard com gerenciamento de tarefas
- ✅ Verificação automática de autenticação
- ✅ Logout com confirmação
- ✅ "Lembrar-me" salvando username
- ✅ Redirecionamentos automáticos
- ✅ Mensagens de sucesso/erro
- ✅ Design dark theme consistente
- ✅ Indicador de força de senha

---

## 🔐 Considerações de Segurança

**O que é protegido:**
- ✅ SQL Injection: PreparedStatements
- ✅ Validação de entrada: Min/Max length
- ✅ Mensagens genéricas: Não revela se usuário existe
- ✅ Sem exposição de senha: Nunca retorna em response

**O que você DEVE fazer em produção:**
- 🔴 Implementar BCrypt para hash de senha
- 🔴 Usar HTTPS (SSL/TLS)
- 🔴 Implementar JWT tokens
- 🔴 Adicionar rate limiting
- 🔴 Usar CORS apropriado
- 🔴 Validação de servidor adicional

---

## 📞 Suporte

Qualquer dúvida sobre o funcionamento das páginas?

Verifique:
1. Console do navegador (F12 → Console) para erros JavaScript
2. Network tab (F12 → Network) para requisições API
3. Application tab (F12 → Application → localStorage) para dados persistidos

---

**Última atualização:** 10/11/2025  
**Versão:** 1.0.0  
**Status:** ✅ Funcional e Pronto para Uso
