package com.example.taskstodo.repository;

import com.example.taskstodo.model.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for managing tasks using JDBC.
 * Handles all database operations for the tasks table.
 */
@Repository
public class TaskRepository {
    private final JdbcTemplate jdbc;

    public TaskRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Initialize the tasks table if it doesn't exist.
     */
    public void initializeTasks() {
        jdbc.execute("CREATE TABLE IF NOT EXISTS tasks (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, " +
                "description TEXT, " +
                "category_id INTEGER, " +
                "user_id INTEGER NOT NULL, " +
                "due_date TEXT, " +
                "completed INTEGER DEFAULT 0, " +
                "created_at TEXT DEFAULT (datetime('now')), " +
                "FOREIGN KEY(category_id) REFERENCES categories(id), " +
                "FOREIGN KEY(user_id) REFERENCES users(id)" +
                ");");
    }

    /**
     * Get all tasks for a specific user.
     */
    public List<Task> findByUserId(Integer userId) {
        return jdbc.query(
                "SELECT id, title, description, category_id, user_id, due_date, completed, created_at " +
                        "FROM tasks WHERE user_id = ? ORDER BY created_at DESC",
                (rs, rowNum) -> new Task(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getObject("category_id") != null ? rs.getInt("category_id") : null,
                        rs.getInt("user_id"),
                        rs.getString("due_date"),
                        rs.getInt("completed"),
                        rs.getString("created_at")
                ),
                userId
        );
    }

    /**
     * Find a task by id.
     */
    public Optional<Task> findById(Integer id) {
        try {
            List<Task> tasks = jdbc.query(
                    "SELECT id, title, description, category_id, user_id, due_date, completed, created_at " +
                            "FROM tasks WHERE id = ?",
                    (rs, rowNum) -> new Task(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getObject("category_id") != null ? rs.getInt("category_id") : null,
                            rs.getInt("user_id"),
                            rs.getString("due_date"),
                            rs.getInt("completed"),
                            rs.getString("created_at")
                    ),
                    id
            );
            return tasks.isEmpty() ? Optional.empty() : Optional.of(tasks.get(0));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Create a new task.
     */
    public Task save(Task task) {
        jdbc.update(
                "INSERT INTO tasks (title, description, category_id, user_id, due_date, completed) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                task.getTitle(),
                task.getDescription(),
                task.getCategoryId(),
                task.getUserId(),
                task.getDueDate(),
                task.getCompleted() != null ? task.getCompleted() : 0
        );
        // Fetch the newly created task
        List<Task> tasks = jdbc.query(
                "SELECT id, title, description, category_id, user_id, due_date, completed, created_at " +
                        "FROM tasks WHERE user_id = ? ORDER BY id DESC LIMIT 1",
                (rs, rowNum) -> new Task(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getObject("category_id") != null ? rs.getInt("category_id") : null,
                        rs.getInt("user_id"),
                        rs.getString("due_date"),
                        rs.getInt("completed"),
                        rs.getString("created_at")
                ),
                task.getUserId()
        );
        return tasks.isEmpty() ? task : tasks.get(0);
    }

    /**
     * Update an existing task.
     */
    public void update(Task task) {
        jdbc.update(
                "UPDATE tasks SET title = ?, description = ?, category_id = ?, due_date = ?, completed = ? WHERE id = ?",
                task.getTitle(),
                task.getDescription(),
                task.getCategoryId(),
                task.getDueDate(),
                task.getCompleted(),
                task.getId()
        );
    }

    /**
     * Delete a task by id.
     */
    public void deleteById(Integer id) {
        jdbc.update("DELETE FROM tasks WHERE id = ?", id);
    }

    /**
     * Mark a task as completed.
     */
    public void markCompleted(Integer id, Integer completed) {
        jdbc.update("UPDATE tasks SET completed = ? WHERE id = ?", completed, id);
    }

    /**
     * Get tasks by category for a specific user.
     */
    public List<Task> findByUserIdAndCategoryId(Integer userId, Integer categoryId) {
        return jdbc.query(
                "SELECT id, title, description, category_id, user_id, due_date, completed, created_at " +
                        "FROM tasks WHERE user_id = ? AND category_id = ? ORDER BY created_at DESC",
                (rs, rowNum) -> new Task(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getObject("category_id") != null ? rs.getInt("category_id") : null,
                        rs.getInt("user_id"),
                        rs.getString("due_date"),
                        rs.getInt("completed"),
                        rs.getString("created_at")
                ),
                userId, categoryId
        );
    }
}
