package com.example.taskstodo.controller;

import com.example.taskstodo.model.User;
import com.example.taskstodo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

/**
 * Controlador de Autenticação
 * 
 * Responsabilidades:
 * - Gerenciar endpoints de registro e login
 * - Validar credenciais do usuário
 * - Retornar respostas HTTP apropriadas
 * - Controlar acesso à aplicação
 * 
 * Endpoints:
 * POST /api/auth/register   - Registrar novo usuário
 * POST /api/auth/login      - Fazer login
 * GET  /api/auth/users/{id} - Obter dados do usuário
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Registra um novo usuário no sistema.
     * 
     * Endpoint: POST /api/auth/register
     * 
     * Request Body:
     * {
     *   "username": "novo_usuario",
     *   "password": "senha_segura"
     * }
     * 
     * Fluxo:
     * 1. Recebe username e password do cliente
     * 2. Valida se os campos não estão vazios
     * 3. Valida comprimento mínimo e máximo
     * 4. Chama UserRepository.register()
     * 5. Retorna user_id e mensagem de sucesso
     * 
     * Response (201 Created):
     * {
     *   "message": "Usuário registrado com sucesso! Faça login.",
     *   "user_id": 1,
     *   "username": "novo_usuario"
     * }
     * 
     * Erros:
     * - 400: Username ou password vazio
     * - 409: Username já existe
     * - 400: Dados fora dos limites de validação
     * 
     * @param body JSON com "username" e "password"
     * @return ResponseEntity com resultado
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        try {
            // Extrai dados do corpo da requisição
            String username = body.get("username");
            String password = body.get("password");

            // Validação de campos obrigatórios
            if (username == null || username.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Nome de usuário é obrigatório"));
            }
            if (password == null || password.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Senha é obrigatória"));
            }

            // Cria objeto User para validação adicional
            User user = new User(username, password);
            
            // Valida dados para registro
            if (!user.isValidForRegistration()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", user.getValidationError()));
            }

            // Registra no banco de dados
            User registeredUser = userRepository.register(username, password);
            
            // Retorna sucesso com status 201 (Created)
            return ResponseEntity.status(201)
                    .body(Map.of(
                        "message", "Usuário registrado com sucesso! Faça login.",
                        "user_id", registeredUser.getId(),
                        "username", registeredUser.getUsername()
                    ));
        } catch (Exception e) {
            // Tratamento de erro específico: username duplicado
            String errorMsg = e.getMessage();
            if (errorMsg != null && errorMsg.contains("já está em uso")) {
                return ResponseEntity.status(409)
                        .body(Map.of("error", "Este nome de usuário já está em uso"));
            }
            
            // Tratamento genérico de erro
            return ResponseEntity.status(400)
                    .body(Map.of("error", errorMsg != null ? errorMsg : "Erro ao registrar"));
        }
    }

    /**
     * Realiza login do usuário.
     * 
     * Endpoint: POST /api/auth/login
     * 
     * Request Body:
     * {
     *   "username": "novo_usuario",
     *   "password": "senha_segura"
     * }
     * 
     * Fluxo:
     * 1. Recebe username e password do cliente
     * 2. Valida se os campos não estão vazios
     * 3. Chama UserRepository.login()
     * 4. Valida credenciais
     * 5. Retorna user_id se sucesso
     * 
     * Response (200 OK):
     * {
     *   "message": "Login realizado com sucesso",
     *   "user_id": 1,
     *   "username": "novo_usuario"
     * }
     * 
     * Erros:
     * - 400: Username ou password vazio
     * - 401: Credenciais inválidas
     * 
     * @param body JSON com "username" e "password"
     * @return ResponseEntity com resultado
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        try {
            // Extrai dados do corpo da requisição
            String username = body.get("username");
            String password = body.get("password");

            // Validação de campos obrigatórios
            if (username == null || username.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Nome de usuário é obrigatório"));
            }
            if (password == null || password.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Senha é obrigatória"));
            }

            // Cria objeto User para validação
            User user = new User(username, password);
            
            // Valida dados para login
            if (!user.isValidForLogin()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Username ou password inválidos"));
            }

            // Autentica no banco de dados
            Optional<User> authenticatedUser = userRepository.login(username, password);
            
            if (authenticatedUser.isPresent()) {
                // Login bem-sucedido - status 200 (OK)
                User loggedInUser = authenticatedUser.get();
                return ResponseEntity.ok(Map.of(
                    "message", "Login realizado com sucesso",
                    "user_id", loggedInUser.getId(),
                    "username", loggedInUser.getUsername()
                ));
            } else {
                // Credenciais inválidas - status 401 (Unauthorized)
                return ResponseEntity.status(401)
                        .body(Map.of("error", "Nome de usuário ou senha inválidos"));
            }
        } catch (Exception e) {
            // Tratamento genérico de erro
            return ResponseEntity.status(400)
                    .body(Map.of("error", "Erro ao fazer login"));
        }
    }

    /**
     * Obtém informações do usuário por ID.
     * 
     * Endpoint: GET /api/auth/users/{id}
     * 
     * Fluxo:
     * 1. Recebe ID do usuário na URL
     * 2. Busca usuário no banco de dados
     * 3. Retorna dados públicos (ID e username)
     * 
     * Response (200 OK):
     * {
     *   "id": 1,
     *   "username": "novo_usuario"
     * }
     * 
     * Erros:
     * - 404: Usuário não encontrado
     * - 500: Erro interno do servidor
     * 
     * Segurança: Retorna apenas dados públicos, não a senha
     * 
     * @param id ID do usuário
     * @return ResponseEntity com dados do usuário
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserInfo(@PathVariable Integer id) {
        try {
            // Busca usuário no banco de dados
            Optional<User> user = userRepository.findById(id);
            
            if (user.isPresent()) {
                User foundUser = user.get();
                // Retorna apenas dados públicos (não inclui password)
                return ResponseEntity.ok(Map.of(
                        "id", foundUser.getId(),
                        "username", foundUser.getUsername()
                ));
            } else {
                // Usuário não encontrado
                return ResponseEntity.status(404)
                        .body(Map.of("error", "Usuário não encontrado"));
            }
        } catch (Exception e) {
            // Erro interno do servidor
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Erro interno do servidor"));
        }
    }
}
