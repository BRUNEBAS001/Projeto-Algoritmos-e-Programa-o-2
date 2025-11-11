# 📅 Funcionalidade: Data Automática do Calendário

## Descrição
Quando você clica em um dia no calendário e tenta adicionar uma tarefa, a data é automaticamente pré-preenchida no formulário de criação.

## Como Funciona

### 1️⃣ Fluxo do Usuário
1. Navegue para a aba **"📅 Calendário"**
2. Clique em qualquer dia do calendário
3. Será aberto um modal com as tarefas daquele dia
4. Clique no botão **"➕ Nova Tarefa"**
5. A página de criar tarefa abrirá com a **data automaticamente preenchida**

### 2️⃣ Mudanças no `calendario.html`

#### Adição de variável global
```javascript
let selectedDate = null; // Armazena a data selecionada
```

#### Modificação da função `showDayTasks()`
```javascript
function showDayTasks(date) {
  selectedDate = date; // Salva a data selecionada
  
  // ... código existente ...
  
  // Atualizar link do botão "Nova Tarefa" com a data formatada
  const dateStr = date.toISOString().split('T')[0]; // Formato: YYYY-MM-DD
  const addTaskBtn = document.querySelector('.btn-modal-add');
  addTaskBtn.href = `/adicionar-tarefa.html?date=${dateStr}`;
  
  // ... resto do código ...
}
```

**O que acontece:**
- A data é convertida para formato ISO (YYYY-MM-DD)
- O link do botão é dinamicamente atualizado com o parâmetro `?date=YYYY-MM-DD`
- Exemplo: `/adicionar-tarefa.html?date=2025-11-15`

### 3️⃣ Mudanças no `adicionar-tarefa.html`

#### Leitura do parâmetro da URL na inicialização
```javascript
window.addEventListener('DOMContentLoaded', function() {
  if (!currentUser) {
    window.location.href = '/login.html';
    return;
  }
  document.getElementById('userDisplay').textContent = '#' + currentUser;
  loadCategories();
  setupCharCounters();
  
  // Pré-preencher data se vindo do calendário
  const params = new URLSearchParams(window.location.search);
  const dateParam = params.get('date');
  if (dateParam) {
    document.getElementById('dueDate').value = dateParam;
  }
});
```

**O que acontece:**
- Ao carregar a página, lê os parâmetros da URL
- Procura pelo parâmetro `?date=...`
- Se existir, pré-preenche o campo de data (`#dueDate`)
- Se não existir (acesso direto), deixa em branco

## ✨ Benefícios

✅ **Experiência melhorada**: Não precisa digitar a data manualmente  
✅ **Menos cliques**: Data já vem preenchida  
✅ **Intuitivo**: O fluxo é natural: clica no dia → cria tarefa nesse dia  
✅ **Retrocompatível**: Se acessar a página diretamente, funciona normalmente  

## 🔄 Fluxo Completo

```
[Calendário]
    ↓
Clica em um dia (ex: 15/11/2025)
    ↓
Modal abre com tarefas do dia
Link do botão fica: /adicionar-tarefa.html?date=2025-11-15
    ↓
Clica em "➕ Nova Tarefa"
    ↓
[Página de Criar Tarefa]
Campo "Data de Vencimento" = 2025-11-15
    ↓
Usuário preenche título e outros dados
    ↓
Clica em "✅ Criar Tarefa"
    ↓
Tarefa criada com a data do calendário!
```

## 📝 Notas Técnicas

- **Formato de data**: ISO 8601 (YYYY-MM-DD)
- **Compatibilidade**: Funciona em todos os navegadores modernos
- **Segurança**: O parâmetro é apenas leitura, sem validação de XSS necessária

## 🧪 Teste Rápido

1. Abra http://localhost:8080/calendario.html
2. Clique em um dia específico (ex: 15 de novembro)
3. Clique em "➕ Nova Tarefa"
4. Verifique se o campo "Data de Vencimento" está preenchido com a data clicada
5. Crie a tarefa

✅ Se tudo funcionar, a tarefa deve aparecer no calendário com a data correta!

## 🎯 Caso de Uso Real

**Cenário**: Você está planejando sua semana e vê que precisa fazer uma tarefa importante no próximo dia 20.

**Antes** (sem a funcionalidade):
1. Clica no dia 20
2. Vê que não tem tarefas
3. Clica em "Nova Tarefa"
4. Precisa se lembrar de digitar "20" no campo de data
5. Digita os dados da tarefa

**Depois** (com a funcionalidade):
1. Clica no dia 20
2. Vê que não tem tarefas
3. Clica em "Nova Tarefa"
4. **Campo de data já está preenchido automaticamente!** ✨
5. Digita os dados da tarefa

**Resultado**: Menos chance de errar a data e fluxo muito mais rápido!
