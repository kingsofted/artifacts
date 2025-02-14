package com.example.hogwarts.hogwarts.artifact.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.example.hogwarts.hogwarts.artifact.Artifact;
import com.example.hogwarts.hogwarts.artifact.dto.ArtifactDto;
import com.example.hogwarts.hogwarts.wizard.converter.WizardToWizardConverter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ArtifactToArtifactDtoConverter implements Converter<Artifact, ArtifactDto> {

    private final WizardToWizardConverter wizardConverter;

    @Override
    public ArtifactDto convert(Artifact artifact) {
        ArtifactDto artifactDto = new ArtifactDto(
                artifact.getId(), 
                artifact.getName(), artifact.getDescription(),
                artifact.getImageUrl(),
                artifact.getOwner() != null ? wizardConverter.convert(artifact.getOwner()) : null
            );

        return artifactDto;
    }

}
