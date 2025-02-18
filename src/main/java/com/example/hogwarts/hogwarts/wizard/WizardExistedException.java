package com.example.hogwarts.hogwarts.wizard;

public class WizardExistedException extends RuntimeException{

    public WizardExistedException(String id){
        super("Wizard with Id " + id + " already exists :(");
    }
}
