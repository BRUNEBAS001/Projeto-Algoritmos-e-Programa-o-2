package com.example.taskstodo.config;

import com.example.taskstodo.repository.CategoryRepository;
import com.example.taskstodo.repository.TaskRepository;
import com.example.taskstodo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Application startup configuration.
 * Initializes default data such as categories, users table, and tasks table on application startup.
 */
@Configuration
public class StartupConfig {

    /**
     * Initialize categories, users table, and tasks table on application startup.
     */
    @Bean
    public CommandLineRunner initializeData(CategoryRepository categoryRepository, UserRepository userRepository, TaskRepository taskRepository) {
        return args -> {
            userRepository.initializeUsers();
            System.out.println("✓ Users table initialized successfully.");
            taskRepository.initializeTasks();
            System.out.println("✓ Tasks table initialized successfully.");
            categoryRepository.initializeCategories();
            System.out.println("✓ Categories initialized successfully.");
        };
    }
}
