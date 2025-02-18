package com.example.hogwarts.hogwarts.wizard.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.example.hogwarts.hogwarts.wizard.Wizard;
import com.example.hogwarts.hogwarts.wizard.dto.WizardDto;

@Component
public class WizardDtoToWizardConverter implements Converter<WizardDto, Wizard>{

    @Override
    public Wizard convert(WizardDto wizardDto) {
        Wizard wizard = new Wizard();
        wizard.setName(wizardDto.name());
        wizard.setId(wizardDto.id());

        return wizard;
    }

}
