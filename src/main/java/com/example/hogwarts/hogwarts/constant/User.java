package com.example.hogwarts.hogwarts.constant;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "test")
@Data
public class User {
    private String username;
    private String age;
}
