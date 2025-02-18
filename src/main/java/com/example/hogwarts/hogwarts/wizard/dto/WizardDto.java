package com.example.hogwarts.hogwarts.wizard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record WizardDto(
        Integer id,

        @NotBlank(message = "Name field cannot be empty")
        String name,

        Integer numberOfArtifacts) {
}
