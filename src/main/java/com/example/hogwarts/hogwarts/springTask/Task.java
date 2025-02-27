package com.example.hogwarts.hogwarts.springTask;

import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Task {

    // Synchronous run
    // @Scheduled(cron ="0-6 * * * *  ?")
    public void executeTask(){
        log.info("Executing spring task: {}", new Date());
    }

    // Asyn task run
}
