package com.example.hogwarts.hogwarts.wizard.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.example.hogwarts.hogwarts.wizard.Wizard;
import com.example.hogwarts.hogwarts.wizard.dto.WizardDto;

@Component
public class WizardToWizardConverter implements Converter<Wizard, WizardDto> {

    @Override
    public WizardDto convert(Wizard wizard) {
        WizardDto wizardDto = new WizardDto(wizard.getId(), wizard.getName(), wizard.getNumberOfArtifacts());

        return wizardDto;
    }

}
