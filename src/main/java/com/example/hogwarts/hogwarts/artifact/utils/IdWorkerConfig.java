package com.example.hogwarts.hogwarts.artifact.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdWorkerConfig {
    @Bean
    public IdWorker idWorker() {
        return new IdWorker(1, 1); // Set appropriate worker & datacenter ID
    }
}

