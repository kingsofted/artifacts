package com.example.hogwarts.hogwarts.artifact;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.hogwarts.hogwarts.artifact.utils.IdWorker;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ArtifactService {

    private final ArtifactRepository artifactRepository;
    private final IdWorker idWorker;

    public Artifact findById(String artifactId) {
        return artifactRepository.findById(artifactId).orElseThrow(
                () -> new ArtifactNotFoundException(artifactId));
    }

    public List<Artifact> findAll() {
        return artifactRepository.findAll();
    }

    public Artifact save(Artifact newArtifact) {
        newArtifact.setId(String.valueOf(idWorker.nextId()));

        return artifactRepository.save(newArtifact);
    }

    public Artifact update(String artifactId, Artifact updatedArtifact) {
        // Fluent interface
        // No need .steam() and .collect because it is not a list
        return this.artifactRepository
                .findById(artifactId)
                .map(existingArtifact -> {
                    existingArtifact.setDescription(updatedArtifact.getDescription());
                    existingArtifact.setName(updatedArtifact.getName());
                    existingArtifact.setImageUrl(updatedArtifact.getImageUrl());
                    return this.artifactRepository.save(existingArtifact);
                })
                .orElseThrow(
                        () -> new ArtifactNotFoundException(artifactId));
    }

    public void delete(String artifactId) {

        this.artifactRepository
                .findById(artifactId)
                .ifPresentOrElse(
                        (artifact) -> {
                            this.artifactRepository.deleteById(artifactId);
                        },
                        () -> {
                            throw new ArtifactNotFoundException(artifactId);
                        });

    }

}
