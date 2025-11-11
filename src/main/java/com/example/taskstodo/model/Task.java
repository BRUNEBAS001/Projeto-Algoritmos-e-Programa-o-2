package com.example.taskstodo.model;

/**
 * Task model representing a user's task/todo item.
 */
public class Task {
    private Integer id;
    private String title;
    private String description;
    private Integer categoryId;
    private Integer userId;
    private String dueDate;
    private Integer completed;
    private String createdAt;

    public Task() {}

    public Task(Integer id, String title, String description, Integer categoryId, 
                Integer userId, String dueDate, Integer completed, String createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.categoryId = categoryId;
        this.userId = userId;
        this.dueDate = dueDate;
        this.completed = completed;
        this.createdAt = createdAt;
    }

    public Task(String title, String description, Integer categoryId, 
                Integer userId, String dueDate, Integer completed) {
        this.title = title;
        this.description = description;
        this.categoryId = categoryId;
        this.userId = userId;
        this.dueDate = dueDate;
        this.completed = completed;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getCompleted() {
        return completed;
    }

    public void setCompleted(Integer completed) {
        this.completed = completed;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", categoryId=" + categoryId +
                ", userId=" + userId +
                ", dueDate='" + dueDate + '\'' +
                ", completed=" + completed +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
