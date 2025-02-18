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
        // Get the value from @AutoFill annotation
        AutoFill autoFillAnnotation = ((MethodSignature) joinPoint.getSignature()).getMethod()
                .getAnnotation(AutoFill.class);
        OperationType operationType = autoFillAnnotation.value();

        log.info("Autofilling: " + operationType);
        // Get the value from the method
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }

        // public Result findWizardById(@PathVariable String wizardId)
        // Take the first argument
        log.info("First parameter: " + args[0]);
    }
}
