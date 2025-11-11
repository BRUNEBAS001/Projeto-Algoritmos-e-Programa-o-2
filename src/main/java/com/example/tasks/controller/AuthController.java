package com.example.tasks.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final JdbcTemplate jdbc;
    public AuthController(JdbcTemplate jdbc){ this.jdbc = jdbc; }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String,String> body){
        try {
            jdbc.update("INSERT INTO users(username,password) VALUES(?,?)", body.get("username"), body.get("password"));
            return ResponseEntity.status(201).build();
        } catch (Exception e){
            return ResponseEntity.status(409).body(Map.of("error","Usuario ja existe"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> body){
        var rows = jdbc.queryForList("SELECT id FROM users WHERE username=? AND password=?", body.get("username"), body.get("password"));
        if(rows.size()>0) return ResponseEntity.ok(Map.of("user_id", rows.get(0).get("id")));
        return ResponseEntity.status(401).build();
    }
}
