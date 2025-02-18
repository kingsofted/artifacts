package com.example.hogwarts.hogwarts.system.exception;

public class ObjectNotFoundException extends RuntimeException{
    public ObjectNotFoundException(String objectName, String id){
        super("Could not found "+ objectName+ " with Id " + id + " :(");
    }
    public ObjectNotFoundException(String objectName, Integer id){
        super("Could not found " + objectName + " with Id " + id + " :(");
    }
}
