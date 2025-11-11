package com.example.tasks.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

/**
 * TaskRepository - Acesso a dados para tarefas e categorias
 * Gerencia operações CRUD no banco SQLite
 */
@Repository
public class TaskRepository {
    private final JdbcTemplate jdbc;
    
    public TaskRepository(JdbcTemplate jdbc){ 
        this.jdbc = jdbc; 
    }

    /**
     * Inicializa as tabelas do banco de dados
     */
    public void initTables(){
        jdbc.execute("PRAGMA foreign_keys = ON;");
        jdbc.execute("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE NOT NULL, password TEXT NOT NULL);"); 
        jdbc.execute("CREATE TABLE IF NOT EXISTS categories (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, user_id INTEGER NOT NULL, FOREIGN KEY(user_id) REFERENCES users(id));"); 
        jdbc.execute("CREATE TABLE IF NOT EXISTS tasks (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, description TEXT, category_id INTEGER, user_id INTEGER NOT NULL, due_date TEXT, completed INTEGER DEFAULT 0, created_at TEXT DEFAULT (datetime('now')), FOREIGN KEY(category_id) REFERENCES categories(id), FOREIGN KEY(user_id) REFERENCES users(id));");
        
        // Inserir categorias padrão
        insertDefaultCategories();
    }

    /**
     * Insere categorias padrão se não existirem
     */
    private void insertDefaultCategories(){
        try {
            Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM categories", Integer.class);
            if (count != null && count == 0) {
                jdbc.update("INSERT INTO categories(name, user_id) VALUES(?, ?)", "Trabalho", 1);
                jdbc.update("INSERT INTO categories(name, user_id) VALUES(?, ?)", "Pessoal", 1);
                jdbc.update("INSERT INTO categories(name, user_id) VALUES(?, ?)", "Saúde", 1);
                jdbc.update("INSERT INTO categories(name, user_id) VALUES(?, ?)", "Educação", 1);
                jdbc.update("INSERT INTO categories(name, user_id) VALUES(?, ?)", "Lazer", 1);
            }
        } catch (Exception e) {
            // Silenciosamente falha se categorias já existem
        }
    }

    /**
     * Lista todas as tarefas de um usuário
     */
    public List<Map<String,Object>> listTasks(int userId){
        return jdbc.queryForList(
            "SELECT id, title, description, category_id as categoryId, user_id as userId, due_date as dueDate, completed, created_at as createdAt FROM tasks WHERE user_id=? ORDER BY created_at DESC", 
            userId
        );
    }

    /**
     * Obtém uma tarefa específica por ID
     */
    public Map<String,Object> getTaskById(int id){
        try {
            return jdbc.queryForMap(
                "SELECT id, title, description, category_id as categoryId, user_id as userId, due_date as dueDate, completed, created_at as createdAt FROM tasks WHERE id=?", 
                id
            );
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Cria uma nova tarefa
     */
    public int createTask(String title, String description, Integer userId, String dueDate, Integer categoryId, boolean completed){
        try {
            return jdbc.update(
                "INSERT INTO tasks(title, description, user_id, due_date, category_id, completed) VALUES(?, ?, ?, ?, ?, ?)", 
                title, 
                description, 
                userId, 
                dueDate, 
                categoryId, 
                completed ? 1 : 0
            );
        } catch (Exception e) {
            System.err.println("Erro ao criar tarefa: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Atualiza uma tarefa existente
     */
    public int updateTask(int id, String title, String description, String dueDate, Integer categoryId, boolean completed){
        try {
            return jdbc.update(
                "UPDATE tasks SET title=?, description=?, due_date=?, category_id=?, completed=? WHERE id=?", 
                title, 
                description, 
                dueDate, 
                categoryId, 
                completed ? 1 : 0, 
                id
            );
        } catch (Exception e) {
            System.err.println("Erro ao atualizar tarefa: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Atualiza apenas o status de conclusão de uma tarefa
     */
    public int updateCompleted(int id, boolean completed){
        try {
            return jdbc.update(
                "UPDATE tasks SET completed=? WHERE id=?", 
                completed ? 1 : 0, 
                id
            );
        } catch (Exception e) {
            System.err.println("Erro ao atualizar status: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Deleta uma tarefa
     */
    public int deleteTask(int id){
        try {
            return jdbc.update("DELETE FROM tasks WHERE id=?", id);
        } catch (Exception e) {
            System.err.println("Erro ao deletar tarefa: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Lista todas as categorias
     */
    public List<Map<String,Object>> listCategories(){
        try {
            return jdbc.queryForList(
                "SELECT id, name FROM categories ORDER BY name"
            );
        } catch (Exception e) {
            System.err.println("Erro ao listar categorias: " + e.getMessage());
            return List.of();
        }
    }
}
