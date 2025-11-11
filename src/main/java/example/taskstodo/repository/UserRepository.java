package com.example.taskstodo.repository;

import com.example.taskstodo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for managing users using JDBC.
 * 
 * Responsabilidades:
 * - Operações CRUD de usuários no banco de dados
 * - Validações de integridade de dados
 * - Proteção contra SQL Injection (using PreparedStatements)
 * - Gerenciamento de credenciais
 * 
 * Nota: Senhas são armazenadas em texto simples (use BCrypt em produção)
 */
@Repository
public class UserRepository {
    private final JdbcTemplate jdbc;

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Inicializa a tabela de usuários se não existir.
     * 
     * Estrutura:
     * - id: Chave primária auto-incrementada
     * - username: Nome de usuário único (não nulo)
     * - password: Senha do usuário (não nulo)
     * - created_at: Timestamp de criação
     */
    public void initializeUsers() {
        jdbc.execute("CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL, " +
                "created_at TEXT DEFAULT (datetime('now'))" +
                ");");
    }

    /**
     * Registra um novo usuário no sistema.
     * 
     * Validações:
     * - Username não pode ser nulo ou vazio
     * - Password não pode ser nulo ou vazio
     * - Username deve ser único (constraint UNIQUE no BD)
     * 
     * @param username Nome de usuário
     * @param password Senha do usuário
     * @return Objeto User com os dados salvos (incluindo ID)
     * @throws Exception Se username já existe ou dados inválidos
     */
    public User register(String username, String password) throws Exception {
        // Validação de entrada
        username = (username != null) ? username.trim() : null;
        password = (password != null) ? password.trim() : null;
        
        if (username == null || username.isEmpty()) {
            throw new Exception("Nome de usuário não pode estar vazio");
        }
        if (password == null || password.isEmpty()) {
            throw new Exception("Senha não pode estar vazia");
        }
        
        // Validação de comprimento
        if (username.length() < 3 || username.length() > 50) {
            throw new Exception("Nome de usuário deve ter entre 3 e 50 caracteres");
        }
        if (password.length() < 6 || password.length() > 100) {
            throw new Exception("Senha deve ter entre 6 e 100 caracteres");
        }

        try {
            // Usa PreparedStatement (via JdbcTemplate) - protegido contra SQL Injection
            jdbc.update(
                    "INSERT INTO users (username, password) VALUES (?, ?)",
                    username, password
            );
            
            // Retorna o usuário recém criado
            return findByUsername(username).orElse(null);
        } catch (Exception e) {
            // Tratamento de erro de constraint UNIQUE
            if (e.getMessage() != null && e.getMessage().contains("UNIQUE")) {
                throw new Exception("Este nome de usuário já está em uso");
            }
            throw e;
        }
    }

    /**
     * Realiza login do usuário verificando username e password.
     * 
     * Fluxo:
     * 1. Busca usuário no banco pelo username
     * 2. Verifica se a senha está correta
     * 3. Retorna o usuário se credenciais válidas
     * 
     * Nota: Implementação atual usa comparação direta de password
     *       Em produção, usar BCrypt para hash de password
     * 
     * @param username Nome de usuário
     * @param password Senha do usuário
     * @return Optional contendo o User se credenciais válidas
     */
    public Optional<User> login(String username, String password) {
        try {
            // Validação de entrada
            if (username == null || username.trim().isEmpty() || 
                password == null || password.trim().isEmpty()) {
                return Optional.empty();
            }
            
            // Usa PreparedStatement - protegido contra SQL Injection
            List<User> users = jdbc.query(
                    "SELECT id, username, password FROM users WHERE username = ? AND password = ?",
                    (rs, rowNum) -> new User(
                        rs.getInt("id"), 
                        rs.getString("username"), 
                        rs.getString("password")
                    ),
                    username.trim(), 
                    password
            );
            
            return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
        } catch (Exception e) {
            // Log do erro em sistema real
            System.err.println("Erro durante login: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Find user by username.
     */
    public Optional<User> findByUsername(String username) {
        try {
            List<User> users = jdbc.query(
                    "SELECT id, username, password FROM users WHERE username = ?",
                    (rs, rowNum) -> new User(rs.getInt("id"), rs.getString("username"), rs.getString("password")),
                    username
            );
            return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Find user by id.
     */
    public Optional<User> findById(Integer id) {
        try {
            List<User> users = jdbc.query(
                    "SELECT id, username, password FROM users WHERE id = ?",
                    (rs, rowNum) -> new User(rs.getInt("id"), rs.getString("username"), rs.getString("password")),
                    id
            );
            return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Update a user.
     */
    public void update(User user) {
        jdbc.update(
                "UPDATE users SET username = ?, password = ? WHERE id = ?",
                user.getUsername(), user.getPassword(), user.getId()
        );
    }

    /**
     * Delete a user by id.
     */
    public void deleteById(Integer id) {
        jdbc.update("DELETE FROM users WHERE id = ?", id);
    }
}
