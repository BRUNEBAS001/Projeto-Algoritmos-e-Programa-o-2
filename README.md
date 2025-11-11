
Task's To Do — Sistema de Gerenciamento de Tarefas

Este repositório contém o projeto "Task's To Do": uma aplicação web para gerenciar tarefas com backend em Spring Boot e frontend em HTML/CSS/JavaScript.

Principais recursos:
- Autenticação de usuários
- Edição completa de tarefas (criar, listar, editar, remover)
- Categorias (Estudo, Trabalho, Pessoal, Urgente)
- Interface responsiva com tema escuro


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
=======
﻿# Projeto Algoritmos e Programacao 2  Site de Tarefas

Este repositório contém o projeto "Task's To Do": um sistema de gerenciamento de tarefas com backend em Spring Boot e frontend em HTML/CSS/JS.

Conteúdo incluído neste repositório:

- Backend: código Java (Spring Boot), `pom.xml`, `src/main/resources/application.properties`, banco SQLite (`src/main/resources/tasks.db`)
- Frontend: pasta `frontend/` com as páginas HTML, CSS e JS
- Documentação: `README_TASKS.md` (documentação completa do projeto)

Instruções rápidas:

1. Verifique se tem Java 17 e Maven instalados.
2. Para compilar: `mvn clean package -DskipTests`
3. Para rodar: `java -jar target/tasks-to-do-1.0.0.jar` (ou use `mvn spring-boot:run`)

Para documentação completa e exemplos de uso, veja `README_TASKS.md`.

---

Se este arquivo estiver sendo gerado a partir de um merge anterior, ele foi consolidado automaticamente para remover conflitos e corrigir problemas de codificação/formatacao.
>>>>>>> cdb0fefe52f1f3cfdd2472fafdf2c20d09f401d8
