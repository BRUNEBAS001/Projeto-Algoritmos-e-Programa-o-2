package com.example.taskstodo.repository;

import com.example.taskstodo.model.Category;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for managing categories using JDBC.
 * Handles database operations for the categories table.
 */
@Repository
public class CategoryRepository {
    private final JdbcTemplate jdbc;

    public CategoryRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Initialize the categories table and insert default categories.
     * Called once during application startup.
     */
    public void initializeCategories() {
        // Create table if it doesn't exist
        jdbc.execute("CREATE TABLE IF NOT EXISTS categories (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL UNIQUE" +
                ");");

        // Insert default categories if they don't exist
        String[] defaultCategories = {"Estudo", "Trabalho", "Pessoal", "Urgente"};
        for (String categoryName : defaultCategories) {
            try {
                jdbc.update(
                        "INSERT INTO categories (name) VALUES (?)",
                        categoryName
                );
            } catch (Exception e) {
                // Category already exists, skip
            }
        }
    }

    /**
     * Retrieve all categories from the database.
     */
    public List<Category> findAll() {
        return jdbc.query(
                "SELECT id, name FROM categories ORDER BY id",
                (rs, rowNum) -> new Category(rs.getInt("id"), rs.getString("name"))
        );
    }

    /**
     * Find a category by id.
     */
    public Category findById(int id) {
        try {
            return jdbc.queryForObject(
                    "SELECT id, name FROM categories WHERE id = ?",
                    (rs, rowNum) -> new Category(rs.getInt("id"), rs.getString("name")),
                    id
            );
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Create a new category.
     */
    public void save(Category category) {
        jdbc.update(
                "INSERT INTO categories (name) VALUES (?)",
                category.getName()
        );
    }

    /**
     * Update an existing category.
     */
    public void update(Category category) {
        jdbc.update(
                "UPDATE categories SET name = ? WHERE id = ?",
                category.getName(), category.getId()
        );
    }

    /**
     * Delete a category by id.
     */
    public void deleteById(int id) {
        jdbc.update("DELETE FROM categories WHERE id = ?", id);
    }
}
