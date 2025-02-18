package com.example.hogwarts.hogwarts.system.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import com.example.hogwarts.hogwarts.artifact.ArtifactNotFoundException;
import com.example.hogwarts.hogwarts.system.Result;
import com.example.hogwarts.hogwarts.system.StatusCode;
import com.example.hogwarts.hogwarts.wizard.WizardExistedException;
import com.example.hogwarts.hogwarts.wizard.WizardNotFoundException;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result handleObjectNotFoundException(ObjectNotFoundException ex){    
        return new Result(false, StatusCode.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler({ArtifactNotFoundException.class, WizardNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result handleObjectNotFoundException(Exception ex){    
        return new Result(false, StatusCode.NOT_FOUND, ex.getMessage());
    }

    //@Valid not pass
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleValidationException(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        
        return new Result(false, StatusCode.INVALID_ARGUMENT, "Provided arguments are invalid, see data for details", errors);
    }

    @ExceptionHandler(WizardExistedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleWizardExisted(WizardExistedException ex){
        return new Result(false, StatusCode.FORBIDDEN, ex.getMessage());
    }

    
}
