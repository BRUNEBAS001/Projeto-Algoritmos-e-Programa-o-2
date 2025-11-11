package com.example.tasks.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.lang.NonNull;

@Configuration
public class DatabaseConfig {
    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName("org.sqlite.JDBC")
                .url("jdbc:sqlite:./data/tarefas.db")
                .build();
    }

    @Bean
    @SuppressWarnings("null")
    public JdbcTemplate jdbcTemplate(@NonNull DataSource ds){ 
        return new JdbcTemplate(ds); 
    }
}
