package com.example.hogwarts.hogwarts.artifact;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hogwarts.hogwarts.artifact.converter.ArtifactDtoToArtifactConverter;
import com.example.hogwarts.hogwarts.artifact.converter.ArtifactToArtifactDtoConverter;
import com.example.hogwarts.hogwarts.artifact.dto.ArtifactDto;
import com.example.hogwarts.hogwarts.system.Result;
import com.example.hogwarts.hogwarts.system.StatusCode;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.endpoint.base-url}/artifacts")
public class ArtifactController {

    private final ArtifactService artifactService;
    private final ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter;
    private final ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter;

    @GetMapping("/{artifactId}")
    public Result findArtifactById(@PathVariable String artifactId) {
        Artifact foundArtifact = artifactService.findById(artifactId);
        ArtifactDto artifactDto = artifactToArtifactDtoConverter.convert(foundArtifact);

        return new Result(true, StatusCode.SUCCESS, "Find One Success", artifactDto);
    }

    @GetMapping
    public Result findAllArtifacts() {
        List<Artifact> artifactList = artifactService.findAll();
        List<ArtifactDto> artifactDtos = artifactList.stream()
                .map(artifactToArtifactDtoConverter::convert)
                .collect(Collectors.toList());

        return new Result(true, StatusCode.SUCCESS, "Find All Success", artifactDtos);
    }

    @PostMapping
    public Result addArtifact(@Valid @RequestBody ArtifactDto artifactDto){
        Artifact newArtifact = artifactDtoToArtifactConverter.convert(artifactDto);
        Artifact savedArtifact = artifactService.save(newArtifact);
        ArtifactDto savedArtifactDto = artifactToArtifactDtoConverter.convert(savedArtifact);
        return new Result(true, StatusCode.SUCCESS, "Added successfully", savedArtifactDto);
    }

    @PutMapping("/{artifactId}")
    public Result updateArtifact(@PathVariable String artifactId,  @Valid @RequestBody ArtifactDto updatedArtifactDto){
        Artifact updatedArtifact = this.artifactService.update(artifactId, artifactDtoToArtifactConverter.convert(updatedArtifactDto));
        ArtifactDto updatedarArtifactDto = artifactToArtifactDtoConverter.convert(updatedArtifact);

        return new Result(true, StatusCode.SUCCESS, "Updated Successfully", updatedarArtifactDto);
    }


    @DeleteMapping("/{artifactId}")
    public Result deleteArtifact(@PathVariable String artifactId){
        this.artifactService.delete(artifactId);

        return new Result(true, StatusCode.SUCCESS, "Deleted Successfully");
    }

}
