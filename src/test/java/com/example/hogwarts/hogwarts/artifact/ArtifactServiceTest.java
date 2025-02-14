package com.example.hogwarts.hogwarts.artifact;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.hogwarts.hogwarts.artifact.utils.IdWorker;
import com.example.hogwarts.hogwarts.wizard.Wizard;

@ExtendWith(MockitoExtension.class)
public class ArtifactServiceTest {

    @Mock
    ArtifactRepository artifactRepository;

    @Mock
    IdWorker idWorker;

    // Inject mocks into this artifactService
    @InjectMocks
    ArtifactService artifactService;

    List<Artifact> artifacts;

    @BeforeEach
    void setUp() {
        this.artifacts = new ArrayList<>();

        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription(
                "A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a1.setImageUrl("ImageUrl");
        this.artifacts.add(a1);

        Artifact a2 = new Artifact();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImageUrl("ImageUrl");
        this.artifacts.add(a2);

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void testFindByIdSuccess() {
        // Given. Arrange inputs and targets. Define the behavior of Mock object
        // artifactRepository
        /*
         * "id": "1250808601744904192"
         * "name": "Invisibility Cloak",
         * "description": "An invisibility cloak is used to make the wearer invisible.",
         * "imageUrl":"ImageUrl",
         */
        Artifact a = new Artifact();
        a.setId("1250808601744904192");
        a.setName("Invisibility Cloak");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setImageUrl("ImageUrl");

        Wizard w = new Wizard();
        w.setId(2);
        w.setName("Harry Potter");

        a.setOwner(w);

        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(a)); // Defines the behavior of
                                                                                              // mock object

        // When. Act on the target behavior. When steps should cover the method to be
        // tested
        Artifact returnArtifact = artifactService.findById("1250808601744904192");

        // Then. Assert expected outcomes.
        assertThat(returnArtifact.getId()).isEqualTo(a.getId());
        assertThat(returnArtifact).isEqualTo(a);
        verify(artifactRepository, times(1)).findById("1250808601744904192");

    }

    @Test
    void testFindByIdNotFound() {
        // Given
        given(artifactRepository.findById(Mockito.any(String.class))).willReturn(Optional.empty());

        // When
        Throwable thrown = catchThrowable(() -> {
            Artifact returnArtifact = artifactService.findById("1250808601744904192");
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ArtifactNotFoundException.class)
                .hasMessage("Could not find artifact with Id 1250808601744904192 :(");
        verify(artifactRepository, times(1)).findById("1250808601744904192");
    }

    @Test
    void testFindAllSuccess() {
        // Given
        given(this.artifactRepository.findAll()).willReturn(this.artifacts);

        // When
        List<Artifact> actualArtifacts = artifactService.findAll();

        // Then
        assertThat(actualArtifacts.size()).isEqualTo(this.artifacts.size());
        verify(artifactRepository, times(1)).findAll();
    }

    @Test
    void testSaveSuccess() {
        // Given
        Artifact newArtifact = new Artifact();
        newArtifact.setName("Artifact 3");
        newArtifact.setDescription("Description...");
        newArtifact.setImageUrl("ImageUrl...");
        given(idWorker.nextId()).willReturn(123456L);
        given(artifactRepository.save(newArtifact)).willReturn(newArtifact);

        // When
        Artifact savedArtifact = artifactService.save(newArtifact);

        // Then
        assertThat(savedArtifact.getName()).isEqualTo(newArtifact.getName());
        assertThat(savedArtifact.getDescription()).isEqualTo("Description...");
        assertThat(savedArtifact.getImageUrl()).isEqualTo("ImageUrl...");
        assertThat(savedArtifact.getId()).isEqualTo(String.valueOf(idWorker.nextId()));
        verify(artifactRepository, times(1)).save(newArtifact);
    }

    @Test
    void testUpdateSuccess() {
        // Given
        Artifact oldArtifact = new Artifact();
        oldArtifact.setId("12345");
        oldArtifact.setName("Deluminator");
        oldArtifact.setDescription("Description...");
        oldArtifact.setImageUrl("imageUrl");
        Artifact updatedArtifact = new Artifact();
        // updatedArtifact.setId("12345");
        updatedArtifact.setName("Deluminator-update");
        updatedArtifact.setDescription("Description...");
        updatedArtifact.setImageUrl("imageUrl-update");

        given(this.artifactRepository.findById(Mockito.any(String.class))).willReturn(Optional.of(oldArtifact));
        given(this.artifactRepository.save(oldArtifact)).willReturn(oldArtifact);

        // When
        // Artifact foundArtifact = this.artifactService.findById("12345");
        Artifact savedArtifact = this.artifactService.update("12345", updatedArtifact);

        // Then
        assertThat(savedArtifact.getId()).isNotEmpty();
        assertThat(savedArtifact.getName()).isEqualTo(updatedArtifact.getName());
        assertThat(savedArtifact.getDescription()).isEqualTo(updatedArtifact.getDescription());
        assertThat(savedArtifact.getImageUrl()).isEqualTo(updatedArtifact.getImageUrl());
        verify(artifactRepository, times(1)).save(Mockito.any(Artifact.class));
        verify(artifactRepository, times(1)).findById("12345");
    }

    @Test
    void testUpdateNotFound() {
        Artifact updatedArtifact = new Artifact();
        updatedArtifact.setName("Deluminator-update");
        updatedArtifact.setDescription("Description...");
        updatedArtifact.setImageUrl("imageUrl-update");
        // Given
        given(this.artifactRepository.findById("12345")).willReturn(Optional.empty());

        // When
        assertThrows(ArtifactNotFoundException.class, () -> {
            this.artifactService.update("12345", updatedArtifact);
        });
        // Then
        verify(artifactRepository, times(1)).findById("12345");
    }

    @Test
    void testDeleteSucess(){
        // Given
        Artifact artifactToBeDeleted = new Artifact();
        artifactToBeDeleted.setId("12345");
        artifactToBeDeleted.setName("Deluminator-update");
        artifactToBeDeleted.setDescription("Description...");
        artifactToBeDeleted.setImageUrl("imageUrl-update");

        given(this.artifactRepository.findById(Mockito.anyString())).willReturn(Optional.of(artifactToBeDeleted));
        doNothing().when(this.artifactRepository).deleteById(Mockito.anyString());

        // When
        artifactService.delete("12345");

        // Then
        verify(this.artifactRepository, times(1)).deleteById(Mockito.anyString());
    }

    @Test
    void testDeleteFailure(){
        // Given
        given(this.artifactRepository.findById(Mockito.anyString())).willReturn(Optional.empty());

        // When
        assertThrows(ArtifactNotFoundException.class, ()-> artifactService.delete("12345"));

        // Then
        verify(this.artifactRepository, times(1)).findById(Mockito.anyString());
        verify(this.artifactRepository, times(0)).deleteById(Mockito.anyString());

    }




}
