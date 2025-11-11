package com.example.taskstodo.model;

/**
 * User model representing an application user.
 * 
 * Responsabilidades:
 * - Representar um usuário do sistema
 * - Armazenar credenciais (username e password)
 * - Validar dados do usuário
 */
public class User {
    
    // Constantes de validação
    private static final int MIN_USERNAME_LENGTH = 3;
    private static final int MAX_USERNAME_LENGTH = 50;
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_PASSWORD_LENGTH = 100;
    
    private Integer id;
    private String username;
    private String password;

    public User() {}

    public User(Integer id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters e Setters com validações

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username != null) {
            this.username = username.trim();
        } else {
            this.username = null;
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Valida se o usuário tem dados válidos para registro
     * @return true se válido, false caso contrário
     */
    public boolean isValidForRegistration() {
        // Verifica se username não é nulo ou vazio
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        
        // Verifica comprimento do username
        if (username.length() < MIN_USERNAME_LENGTH || username.length() > MAX_USERNAME_LENGTH) {
            return false;
        }
        
        // Verifica se password não é nulo ou vazio
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        
        // Verifica comprimento da password
        if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            return false;
        }
        
        return true;
    }

    /**
     * Valida se o usuário tem dados válidos para login
     * @return true se válido, false caso contrário
     */
    public boolean isValidForLogin() {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * Obtém mensagem de erro de validação
     * @return Descrição do erro de validação
     */
    public String getValidationError() {
        if (username == null || username.trim().isEmpty()) {
            return "Nome de usuário é obrigatório";
        }
        
        if (username.length() < MIN_USERNAME_LENGTH) {
            return "Nome de usuário deve ter pelo menos " + MIN_USERNAME_LENGTH + " caracteres";
        }
        
        if (username.length() > MAX_USERNAME_LENGTH) {
            return "Nome de usuário não pode ter mais de " + MAX_USERNAME_LENGTH + " caracteres";
        }
        
        if (password == null || password.trim().isEmpty()) {
            return "Senha é obrigatória";
        }
        
        if (password.length() < MIN_PASSWORD_LENGTH) {
            return "Senha deve ter pelo menos " + MIN_PASSWORD_LENGTH + " caracteres";
        }
        
        if (password.length() > MAX_PASSWORD_LENGTH) {
            return "Senha não pode ter mais de " + MAX_PASSWORD_LENGTH + " caracteres";
        }
        
        return "";
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
