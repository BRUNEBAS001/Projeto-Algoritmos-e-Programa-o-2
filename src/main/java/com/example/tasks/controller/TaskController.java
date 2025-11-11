package com.example.tasks.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.example.tasks.repository.TaskRepository;
import java.util.Map;

/**
 * TaskController - Endpoints para CRUD de tarefas
 * Gerencia criação, leitura, atualização e exclusão de tarefas
 */
@RestController
@RequestMapping("/api")
public class TaskController {
    private final TaskRepository repo;
    
    public TaskController(TaskRepository repo){ 
        this.repo = repo; 
        this.repo.initTables(); 
    }

    /**
     * GET /api/tasks/user/{userId} - Listar tarefas do usuário
     */
    @GetMapping("/tasks/user/{userId}")
    public ResponseEntity<?> listByUser(@PathVariable int userId){ 
        return ResponseEntity.ok(repo.listTasks(userId)); 
    }

    /**
     * GET /api/tasks/{id} - Obter tarefa por ID
     */
    @GetMapping("/tasks/{id}")
    public ResponseEntity<?> getById(@PathVariable int id){ 
        return ResponseEntity.ok(repo.getTaskById(id)); 
    }

    /**
     * POST /api/tasks - Criar nova tarefa
     * Body: { title, description, user_id, due_date, category_id, completed }
     */
    @PostMapping("/tasks")
    public ResponseEntity<?> create(@RequestBody Map<String,Object> body){
        try {
            String title = (String)body.get("title");
            String description = (String)body.getOrDefault("description", "");
            
            // Parse user_id - pode vir como String ou Number
            Integer userId = null;
            Object userIdObj = body.get("user_id");
            if (userIdObj instanceof Number) {
                userId = ((Number)userIdObj).intValue();
            } else if (userIdObj instanceof String) {
                userId = Integer.parseInt((String)userIdObj);
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "user_id inválido"));
            }
            
            String dueDate = (String)body.getOrDefault("due_date", null);
            
            // Parse category_id
            Integer categoryId = null;
            Object catIdObj = body.get("category_id");
            if (catIdObj instanceof Number) {
                categoryId = ((Number)catIdObj).intValue();
            } else if (catIdObj instanceof String && !((String)catIdObj).isEmpty()) {
                categoryId = Integer.parseInt((String)catIdObj);
            }
            
            if (title == null || title.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Título é obrigatório"));
            }
            
            int result = repo.createTask(title, description, userId, dueDate, categoryId, false);
            if (result > 0) {
                return ResponseEntity.status(201).body(Map.of("id", result));
            } else {
                return ResponseEntity.status(500).body(Map.of("error", "Erro ao criar tarefa"));
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Erro ao converter números: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Erro: " + e.getMessage()));
        }
    }

    /**
     * PUT /api/tasks/{id} - Atualizar tarefa
     * Body: { title, description, due_date, category_id, completed }
     */
    @PutMapping("/tasks/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody Map<String,Object> body){
        try {
            String title = (String)body.get("title");
            String description = (String)body.getOrDefault("description", "");
            String dueDate = (String)body.getOrDefault("due_date", null);
            
            // Parse category_id
            Integer categoryId = null;
            Object catIdObj = body.get("category_id");
            if (catIdObj instanceof Number) {
                categoryId = ((Number)catIdObj).intValue();
            } else if (catIdObj instanceof String && !((String)catIdObj).isEmpty()) {
                categoryId = Integer.parseInt((String)catIdObj);
            }
            
            // Parse completed
            Boolean completed = false;
            Object completedObj = body.get("completed");
            if (completedObj instanceof Boolean) {
                completed = (Boolean)completedObj;
            } else if (completedObj instanceof Number) {
                completed = ((Number)completedObj).intValue() != 0;
            } else if (completedObj instanceof String) {
                completed = Boolean.parseBoolean((String)completedObj);
            }
            
            if (title == null || title.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Título é obrigatório"));
            }
            
            int result = repo.updateTask(id, title, description, dueDate, categoryId, completed);
            if (result > 0) {
                return ResponseEntity.ok(Map.of("message", "Tarefa atualizada"));
            } else {
                return ResponseEntity.status(404).body(Map.of("error", "Tarefa não encontrada"));
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Erro ao converter números: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Erro: " + e.getMessage()));
        }
    }

    /**
     * PATCH /api/tasks/{id}/completed - Marcar tarefa como concluída/pendente
     * Body: { completed: true/false }
     */
    @PatchMapping("/tasks/{id}/completed")
    public ResponseEntity<?> toggleCompleted(@PathVariable int id, @RequestBody Map<String,Object> body){
        try {
            Boolean completed = (Boolean)body.get("completed");
            int result = repo.updateCompleted(id, completed);
            if (result > 0) {
                return ResponseEntity.ok(Map.of("message", "Status atualizado"));
            } else {
                return ResponseEntity.status(404).body(Map.of("error", "Tarefa não encontrada"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Erro: " + e.getMessage()));
        }
    }

    /**
     * DELETE /api/tasks/{id} - Deletar tarefa
     */
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<?> delete(@PathVariable int id){ 
        try {
            int result = repo.deleteTask(id);
            if (result > 0) {
                return ResponseEntity.ok(Map.of("message", "Tarefa deletada"));
            } else {
                return ResponseEntity.status(404).body(Map.of("error", "Tarefa não encontrada"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Erro: " + e.getMessage()));
        }
    }

    /**
     * GET /api/categories - Listar todas as categorias
     */
    @GetMapping("/categories")
    public ResponseEntity<?> listCategories(){
        return ResponseEntity.ok(repo.listCategories());
    }
}
