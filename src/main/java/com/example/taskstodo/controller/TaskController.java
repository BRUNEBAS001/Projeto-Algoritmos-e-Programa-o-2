package com.example.taskstodo.controller;

import com.example.taskstodo.model.Task;
import com.example.taskstodo.repository.TaskRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Task controller handling CRUD operations for tasks.
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Get all tasks for a user.
     * GET /api/tasks/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getTasksByUser(@PathVariable Integer userId) {
        try {
            List<Task> tasks = taskRepository.findByUserId(userId);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Failed to fetch tasks"));
        }
    }

    /**
     * Get a specific task by id.
     * GET /api/tasks/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Integer id) {
        try {
            Optional<Task> task = taskRepository.findById(id);
            if (task.isPresent()) {
                return ResponseEntity.ok(task.get());
            } else {
                return ResponseEntity.status(404)
                        .body(Map.of("error", "Task not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Failed to fetch task"));
        }
    }

    /**
     * Create a new task.
     * POST /api/tasks
     */
    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody Map<String, Object> body) {
        try {
            String title = (String) body.get("title");
            String description = (String) body.get("description");
            Integer userId = ((Number) body.get("user_id")).intValue();
            Integer categoryId = body.get("category_id") != null ? 
                    ((Number) body.get("category_id")).intValue() : null;
            String dueDate = (String) body.get("due_date");

            if (title == null || title.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Title is required"));
            }

            Task task = new Task(title, description, categoryId, userId, dueDate, 0);
            Task savedTask = taskRepository.save(task);
            return ResponseEntity.status(201).body(savedTask);
        } catch (Exception e) {
            return ResponseEntity.status(400)
                    .body(Map.of("error", "Failed to create task: " + e.getMessage()));
        }
    }

    /**
     * Update an existing task.
     * PUT /api/tasks/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Integer id, @RequestBody Map<String, Object> body) {
        try {
            Optional<Task> existingTask = taskRepository.findById(id);
            if (!existingTask.isPresent()) {
                return ResponseEntity.status(404)
                        .body(Map.of("error", "Task not found"));
            }

            Task task = existingTask.get();
            
            if (body.containsKey("title")) {
                task.setTitle((String) body.get("title"));
            }
            if (body.containsKey("description")) {
                task.setDescription((String) body.get("description"));
            }
            if (body.containsKey("category_id")) {
                task.setCategoryId(body.get("category_id") != null ? 
                        ((Number) body.get("category_id")).intValue() : null);
            }
            if (body.containsKey("due_date")) {
                task.setDueDate((String) body.get("due_date"));
            }
            if (body.containsKey("completed")) {
                task.setCompleted(((Number) body.get("completed")).intValue());
            }

            taskRepository.update(task);
            return ResponseEntity.ok(Map.of("message", "Task updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(400)
                    .body(Map.of("error", "Failed to update task: " + e.getMessage()));
        }
    }

    /**
     * Delete a task.
     * DELETE /api/tasks/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Integer id) {
        try {
            Optional<Task> task = taskRepository.findById(id);
            if (!task.isPresent()) {
                return ResponseEntity.status(404)
                        .body(Map.of("error", "Task not found"));
            }

            taskRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Task deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Failed to delete task"));
        }
    }

    /**
     * Mark task as completed/incomplete.
     * PATCH /api/tasks/{id}/completed
     */
    @PatchMapping("/{id}/completed")
    public ResponseEntity<?> toggleCompleted(@PathVariable Integer id, @RequestBody Map<String, Integer> body) {
        try {
            Optional<Task> task = taskRepository.findById(id);
            if (!task.isPresent()) {
                return ResponseEntity.status(404)
                        .body(Map.of("error", "Task not found"));
            }

            Integer completed = body.get("completed");
            taskRepository.markCompleted(id, completed);
            return ResponseEntity.ok(Map.of("message", "Task status updated"));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Failed to update task status"));
        }
    }

    /**
     * Get tasks by category for a user.
     * GET /api/tasks/user/{userId}/category/{categoryId}
     */
    @GetMapping("/user/{userId}/category/{categoryId}")
    public ResponseEntity<?> getTasksByCategory(@PathVariable Integer userId, @PathVariable Integer categoryId) {
        try {
            List<Task> tasks = taskRepository.findByUserIdAndCategoryId(userId, categoryId);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Failed to fetch tasks"));
        }
    }
}
