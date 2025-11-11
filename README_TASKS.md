# Task's To Do — Gerenciador de Tarefas

## Descrição das Funcionalidades do Sistema

O sistema **Task's To Do** é uma aplicação web desenvolvida com Java (Spring Boot) no backend, cuja finalidade é permitir que o usuário organize suas atividades diárias de maneira simples e eficiente. O site possui uma interface de uso intuitivo e trabalha com persistência de dados, garantindo que as tarefas cadastradas sejam armazenadas e recuperadas de forma confiável.

---

## 🔐 1. Login de Usuários

- O sistema conta com um módulo de autenticação, onde cada usuário possui um cadastro próprio.
- Somente usuários autenticados podem acessar suas tarefas.
- Isso garante segurança e individualização das listas.
- Cada sessão é gerenciada de forma segura no banco de dados SQLite.

---

## ✅ 2. Cadastro de Tarefas

O usuário pode registrar novas tarefas de forma rápida e intuitiva.

Para cada tarefa podem ser fornecidos dados como:

- **Título da tarefa** — descrição breve da atividade
- **Descrição opcional** — detalhes adicionais sobre a tarefa
- **Categoria** — organização por tipo (Estudo, Trabalho, Pessoal, Urgente)
- **Data de vencimento** — prazo para conclusão da atividade

O cadastro é salvo no banco de dados, garantindo que as informações permaneçam disponíveis mesmo após fechar o navegador ou sair da aplicação.

---

## 👁️ 3. Visualização das Tarefas

- O sistema exibe todas as tarefas cadastradas de forma clara e organizada.
- As tarefas podem ser listadas por ordem de criação ou agrupadas por categoria.
- A visualização é atualizada automaticamente a cada operação (adicionar, editar ou remover).
- Interface responsiva que se adapta a diferentes tamanhos de tela.

---

## ✏️ 4. Edição de Tarefas

Caso o usuário precise alterar alguma informação, o sistema permite editar uma tarefa já existente.

- O usuário pode modificar título, descrição, categoria ou data de vencimento a qualquer momento.
- Após a edição, o registro é atualizado imediatamente no banco de dados.
- Nenhuma perda de dados — todas as alterações são salvas com segurança.

---

## 🗑️ 5. Exclusão de Tarefas

Também é possível apagar tarefas que não são mais necessárias.

- A remoção é definitiva e segura.
- Após a exclusão, a lista é atualizada para o usuário em tempo real.
- Confirmação de exclusão para evitar remoções acidentais.

---

## 🌙 6. Interface Moderna com Tema Escuro

O projeto utiliza um **tema escuro (Dark Mode)**, proporcionando:

- **Conforto visual** — reduz fadiga ocular em sessões prolongadas
- **Layout moderno e intuitivo** — design atual e agradável
- **Melhor experiência** — especialmente em ambientes de pouca luz
- **Design responsivo** — permite acesso por diferentes tamanhos de tela, incluindo:
  - 💻 Computadores desktop
  - 📱 Tablets
  - 📲 Dispositivos móveis

---

## 🛠️ Tecnologias Utilizadas

- **Backend:** Java 17+ com Spring Boot 3.2.3
- **Banco de Dados:** SQLite
- **Frontend:** HTML5, CSS3 com tema escuro, JavaScript vanilla
- **Build:** Maven
- **Gerenciamento de Dependências:** Spring Data JDBC

---

## 🚀 Como Executar

### Pré-requisitos
- Java 17 ou superior instalado
- Maven 3.6+ instalado

### Passos para executar

1. **Clone ou extraia o projeto**
   ```bash
   cd tasks-to-do-springboot
   ```

2. **Execute a aplicação**
   ```bash
   mvn clean spring-boot:run
   ```
   Ou compile e empacote:
   ```bash
   mvn clean package
   java -jar target/tasks-to-do-1.0.0.jar
   ```

3. **Acesse a aplicação**
   - Abra seu navegador e acesse: `http://localhost:8080`

---

## 📊 Estrutura do Banco de Dados

### Tabela: `users`
- `id` (INTEGER PRIMARY KEY)
- `username` (TEXT UNIQUE NOT NULL)
- `password` (TEXT NOT NULL)
- `created_at` (TEXT)

### Tabela: `categories`
- `id` (INTEGER PRIMARY KEY)
- `name` (TEXT UNIQUE NOT NULL)
- Categorias padrão: Estudo, Trabalho, Pessoal, Urgente

### Tabela: `tasks`
- `id` (INTEGER PRIMARY KEY)
- `title` (TEXT NOT NULL)
- `description` (TEXT)
- `category_id` (INTEGER, FK)
- `user_id` (INTEGER, FK)
- `due_date` (TEXT)
- `completed` (INTEGER)
- `created_at` (TEXT)

---

## ⚙️ Endpoints da API

### Autenticação
- `POST /api/auth/register` — Registrar novo usuário
- `POST /api/auth/login` — Fazer login

### Tarefas
- `GET /api/users/{id}/tasks` — Listar tarefas do usuário
- `POST /api/tasks` — Criar nova tarefa
- `PUT /api/tasks/{id}` — Atualizar tarefa
- `DELETE /api/tasks/{id}` — Deletar tarefa

---

## ⚠️ Observações Importantes

### Desenvolvimento
- Senhas estão em **texto simples** (apenas para fins educacionais).

### Produção
- ⚠️ **NUNCA** usar senhas em texto plano em produção.
- Implementar **Spring Security** com **BCrypt** para hashing de senhas.
- Configurar **HTTPS/SSL** para comunicação segura.
- Usar variáveis de ambiente para configurações sensíveis.
- Implementar **CSRF protection** e validação robusta de entrada.

---

## 📝 Licença

Este projeto é fornecido como exemplo educacional.

---

## 👨‍💻 Desenvolvedor

Desenvolvido com Spring Boot 3.2.3 e SQLite.
