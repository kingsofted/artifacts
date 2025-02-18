package com.example.hogwarts.hogwarts.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Slf4j
public class AutoFillAspect {

    @Pointcut("@annotation(AutoFill)")
    public void printField() {
    }

    @Before("printField()")
    public void autoFillMethod(JoinPoint joinPoint) {
        // Log or print when the method with @AutoFill is invoked
        AutoFill autoFillAnnotation = ((MethodSignature) joinPoint.getSignature()).getMethod()
                .getAnnotation(AutoFill.class);
        OperationType operationType = autoFillAnnotation.value(); // Get the value from @AutoFill annotation
        
        log.info("Autofilling for Operation: {}", operationType);
        System.out.println("Autofilling for Operation: " + operationType);
    }
}
