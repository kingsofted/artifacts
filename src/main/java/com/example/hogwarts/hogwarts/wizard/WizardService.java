package com.example.hogwarts.hogwarts.wizard;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hogwarts.hogwarts.artifact.Artifact;
import com.example.hogwarts.hogwarts.artifact.ArtifactNotFoundException;
import com.example.hogwarts.hogwarts.artifact.ArtifactRepository;
import com.example.hogwarts.hogwarts.artifact.ArtifactService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WizardService {

    private final WizardRepository wizardRepository;
    private final ArtifactService artifactService;
    private final ArtifactRepository artifactRepository;

    public Wizard findWizardbyId(String wizardId) {
        return wizardRepository.findById(Integer.parseInt(wizardId))
                .orElseThrow(() -> new WizardNotFoundException(wizardId));
    }

    public List<Wizard> findAllWizards() {
        return wizardRepository.findAll();
    }

    public Wizard addWizard(Wizard wizard) {
        Optional<Wizard> existingWizard = this.wizardRepository.findByName(wizard.getName());
        if (existingWizard.isEmpty()) {
            return wizardRepository.save(wizard);
        }
        throw new WizardExistedException(String.valueOf(wizard.getId()));
    }

    public Wizard updateWizard(String wizardId, Wizard wizard) {
        // Fluent interface
        // No need .steam() and .collect because it is not a list
        return this.wizardRepository
                .findById(Integer.parseInt(wizardId))
                .map((existingWizard) -> {
                    existingWizard.setName(wizard.getName());
                    return this.wizardRepository.save(existingWizard);
                })
                .orElseThrow(
                        () -> new WizardNotFoundException(wizardId));
        // if(existingWizard.isPresent()){
        // existingWizard.get().setName(wizard.getName());
        // return wizardRepository.save(existingWizard.get());
        // }
        // throw new WizardNotFoundException(wizardId);
    }

    public Wizard deleteWizard(String wizardId){
        return this.wizardRepository
            .findById(Integer.parseInt(wizardId))
            .map((existingWizard) -> {
                // Not efficient
                // List<Artifact> artifacts = existingWizard.getArtifacts();
                // artifacts.forEach(artifact-> artifact.setOwner(null));
                // this.artifactRepository.saveAll(artifacts);

                // Do this instead
                // When wizard.findById is called, hibernate start tracking all the entities, if any of the entities has change it will automaticall update without the need to call repository.update
                existingWizard.removeAllArtifacts();
                this.wizardRepository.deleteById(Integer.parseInt(wizardId));
                return existingWizard;
            })
            .orElseThrow(
                () -> new WizardNotFoundException(wizardId)
            );
    }

    public Wizard addArtifactsToWizard(String wizardId, List<String> artifactIds){
        Wizard wizard = this.wizardRepository.findById(Integer.parseInt(wizardId)).orElseThrow(()->
            new WizardNotFoundException(wizardId)
        );

        artifactIds.stream()
            .forEach((Id) -> {
                Artifact artifact = this.artifactService.findById(Id);
                wizard.addArtifact(artifact);
            });
            
        this.wizardRepository.save(wizard);

        // Artifact existingArtifact = this.artifactService.findById(artifact.getId());
        // wizard.addArtifact(existingArtifact);

        return wizard;
    }

}
