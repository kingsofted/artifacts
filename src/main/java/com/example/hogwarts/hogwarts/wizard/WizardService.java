package com.example.hogwarts.hogwarts.wizard;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WizardService {

    private final WizardRepository wizardRepository;

    public Wizard findWizardbyId(String wizardId) {
        return wizardRepository.findById(Integer.parseInt(wizardId)).orElseThrow(()-> new Error());
    }

}
