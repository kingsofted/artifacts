package com.example.hogwarts.hogwarts.wizard;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hogwarts.hogwarts.system.Result;
import com.example.hogwarts.hogwarts.system.StatusCode;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class WizardController {

    private final WizardService wizardService;

    @GetMapping("/wizard/{wizardId}")
    public Result findWizardById(@PathVariable String wizardId){
        Wizard wizard = wizardService.findWizardbyId(wizardId);

        return new Result(true, StatusCode.SUCCESS, "Find One Success", wizard);
    }

}
