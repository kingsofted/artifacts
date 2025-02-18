package com.example.hogwarts.hogwarts.wizard;

public class WizardNotFoundException extends RuntimeException{

    public WizardNotFoundException(String id){
        super("Could not find artifact with Id " + id + " :(");
    }

}
