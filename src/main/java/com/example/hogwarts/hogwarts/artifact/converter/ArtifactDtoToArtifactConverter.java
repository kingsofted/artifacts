package com.example.hogwarts.hogwarts.artifact.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.example.hogwarts.hogwarts.artifact.Artifact;
import com.example.hogwarts.hogwarts.artifact.dto.ArtifactDto;


@Component
public class ArtifactDtoToArtifactConverter implements Converter<ArtifactDto, Artifact>{

    @Override
    public Artifact convert(ArtifactDto value) {
        Artifact artifact = new Artifact();
        artifact.setName(value.name());
        artifact.setId(value.id());
        artifact.setDescription(value.description());
        artifact.setImageUrl(value.imageUrl());

        return artifact;
    }

}
