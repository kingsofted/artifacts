package com.example.hogwarts.hogwarts.wizard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.lang.StackWalker.Option;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.example.hogwarts.hogwarts.artifact.Artifact;
import com.example.hogwarts.hogwarts.artifact.ArtifactNotFoundException;
import com.example.hogwarts.hogwarts.artifact.ArtifactRepository;
import com.example.hogwarts.hogwarts.artifact.ArtifactService;
import com.example.hogwarts.hogwarts.wizard.converter.WizardToWizardDtoConverter;
import com.example.hogwarts.hogwarts.wizard.dto.WizardDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@ExtendWith(MockitoExtension.class)
public class WizardServiceTest {

    @Mock
    private WizardRepository wizardRepository;

    @Mock
    private ArtifactRepository artifactRepository;

    @Mock
    private ArtifactService artifactService;

    @InjectMocks
    private WizardService wizardService;

    @Autowired
    private WizardToWizardDtoConverter wizardDtoConverter;

    Wizard w;
    Wizard w2;
    WizardDto wDto;
    Artifact a;
    Artifact b;
    List<Wizard> listWizards;

    @BeforeEach
    void setUp(){
        this.listWizards = new ArrayList<>();
        
        w = new Wizard();
        w.setId(1234);
        w.setName("Wizard King");

        Artifact a = new Artifact();
        a.setId("1250808601744904191");
        a.setName("Deluminator");
        a.setDescription(
                "A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a.setImageUrl("ImageUrl");

        b = new Artifact();
        b.setId("1242154125");
        b.setName("B type");
        b.setDescription(
                "This is B description");
        b.setImageUrl("ImageUrl");


        w.addArtifact(a);

        this.listWizards.add(w);

        // wDto = wizardDtoConverter.convert(w2);

        w2 = new Wizard();
        w2.setId(1235); 
        w2.setName("Wizard Queen");


        
        this.listWizards.add(w2); 
    }


    @AfterEach
    void setDown(){

    }

    @Test
    void testFindWizardbyIdSuccess() {
        // Given
        given(this.wizardRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.of(w));

        // When 
        Wizard returnWizard = this.wizardService.findWizardbyId("1234");

        // Then
        assertThat(returnWizard.getId()).isEqualTo(w.getId());
        assertThat(returnWizard.getName()).isEqualTo(w.getName());
        assertThat(returnWizard.getArtifacts()).isEqualTo(w.getArtifacts());
        verify(this.wizardRepository, times(1)).findById(Mockito.anyInt());
    }

    @Test
    void testFindWizardbyIdNotFound() {
        // Given
        given(this.wizardRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        // When & Then
        assertThrows(WizardNotFoundException.class, () -> {
            this.wizardService.findWizardbyId("12345");
        });

        // Verify
        verify(this.wizardRepository, times(1)).findById(anyInt());
    }

    @Test
    void testFindAllWizardSuccess(){
        // Given
        given(this.wizardRepository.findAll()).willReturn(this.listWizards);

        // When
        List<Wizard> wizards = this.wizardRepository.findAll();
        // Then
        assertThat(wizards.size()).isEqualTo(2);
        // assertThat(wizards.get(0)).isEqualTo("Wizard King");
        // assertThat(wizards.get(1)).isEqualTo("wizard Queen");
        verify(this.wizardRepository, times(1)).findAll();
    }

    @Test
    void testFindAllWizardFailure(){
        // Given
        given(this.wizardRepository.findAll()).willReturn(null);

        // When
        List<Wizard> wizards = this.wizardRepository.findAll();
        // Then
        assertThat(wizards).isNull();;
        verify(this.wizardRepository, times(1)).findAll();
    }

    @Test
    void testAddWizardSuccess(){
        // Given
        given(this.wizardRepository.save(Mockito.any(Wizard.class))).willReturn(w);
        given(this.wizardRepository.findByName(Mockito.anyString())).willReturn(Optional.empty());

        // When
        Wizard wizard = this.wizardService.addWizard(w);

        // Then
        assertThat(wizard.getName()).isEqualTo(w.getName());
        assertThat(wizard.getArtifacts()).isNotEmpty();
        verify(this.wizardRepository, times(1)).save(Mockito.any(Wizard.class));
        verify(this.wizardRepository, times(1)).findByName(Mockito.anyString());
    }

    @Test
    void testAddWizardIdFailure(){
        // Given
        given(this.wizardRepository.findByName(Mockito.anyString())).willReturn(Optional.of(w));

        // When & Then
        assertThrows(WizardExistedException.class, ()->{
            this.wizardService.addWizard(w);
        });

        verify(this.wizardRepository, times(1)).findByName(Mockito.anyString());
    }

    @Test
    void testUpdateWizardSuccess(){
        // Given
        given(this.wizardRepository.findById(Mockito.anyInt())).willReturn(Optional.of(w));
        given(this.wizardRepository.save(Mockito.any(Wizard.class))).willReturn(w);

        // When 
        Wizard wizard = this.wizardService.updateWizard("123", w2);

        // Then
        assertThat(wizard.getId()).isEqualTo(w.getId());
        assertThat(wizard.getName()).isEqualTo(w.getName());
        assertThat(wizard.getArtifacts().size()).isEqualTo(1);

        verify(this.wizardRepository, times(1)).findById(Mockito.anyInt());
        verify(this.wizardRepository, times(1)).save(Mockito.any(Wizard.class));   
    }

    @Test
    void testUpdateWizardNotFound(){
        // Given
        given(this.wizardRepository.findById(Mockito.anyInt())).willReturn(Optional.empty());

        // When 
        assertThrows(WizardNotFoundException.class, ()->{
            this.wizardService.updateWizard("123", w);
        });

        // Then
        verify(this.wizardRepository, times(1)).findById(Mockito.anyInt());
        verify(this.wizardRepository, times(0)).save(Mockito.any(Wizard.class));   
    }

    @Test
    void testDeleteWizardSuccess(){
        // Given
        w.setId(1234);
        given(this.wizardRepository.findById(Mockito.anyInt())).willReturn(Optional.of(w));

        doNothing().when(this.wizardRepository).deleteById(Mockito.anyInt());
        // given(this.artifactRepository.saveAll(Mockito.<Artifact>anyList())).willReturn(null);

        // When
        Wizard wizard = this.wizardService.deleteWizard("1234");

        // Then
        assertThat(wizard.getId()).isEqualTo(1234);
        assertThat(wizard.getName()).isEqualTo(wizard.getName());

        verify(this.wizardRepository, times(1)).findById(Mockito.anyInt());
        verify(this.wizardRepository, times(1)).deleteById(Mockito.anyInt());
    }

    @Test
    void testDeleteWizardNotFound(){
        // Given
        given(this.wizardRepository.findById(Mockito.anyInt())).willReturn(Optional.empty());

        // When
        assertThrows(WizardNotFoundException.class, () -> {
            Wizard wizard = this.wizardService.deleteWizard("1234");
        });

        // Then
        verify(this.wizardRepository, times(1)).findById(Mockito.anyInt());
        verify(this.wizardRepository, times(0)).deleteById(Mockito.anyInt());
    }


    @Test
    void testAddArtifactsToWizardSuccess(){
        // Given
        w.setId(1234);
        Artifact c = new Artifact();
        c.setId("4364537");
        c.setName("C type");
        c.setDescription(
        "This is C description");
        c.setImageUrl("ImageUrl");
        List<String> artifactIds = Arrays.asList( "456", "789");

        given(this.wizardRepository.findById(Mockito.anyInt())).willReturn(Optional.of(w));
        given(this.artifactService.findById("789")).willReturn(c);
        given(this.artifactService.findById("456")).willReturn(b);
        given(this.wizardRepository.save(Mockito.any(Wizard.class))).willReturn(w);

        // When
        System.out.println("Before artifact size: " + w.getArtifacts().size());
        Wizard returnedWizard = this.wizardService.addArtifactsToWizard("1234", artifactIds);
        System.out.println("After artifact size: " + returnedWizard.getArtifacts().size());
        assertThat(returnedWizard.getId()).isEqualTo(1234);
        assertThat(returnedWizard.getArtifacts().size()).isEqualTo(3);
        
        
        // Then
        verify(this.wizardRepository, times(1)).findById(Mockito.anyInt());
        verify(this.artifactService, times(2)).findById(Mockito.anyString());
        verify(this.wizardRepository, times(1)).save(Mockito.any(Wizard.class));
    }

    @Test
    void testAddArtifactsToWizardNotFoundArtifact(){
        // Given
        w.setId(1234);
        List<String> artifactIds = Arrays.asList("123", "456");
        Artifact existingArtifactToBeAdded = new Artifact();
        existingArtifactToBeAdded.setId("235346534634");
        existingArtifactToBeAdded.setName("Testing artifact");
        existingArtifactToBeAdded.setDescription(
                "A Testing (as well as return) the light from any light source to provide cover to the user.");
                existingArtifactToBeAdded.setImageUrl("ImageUrl");

        given(this.wizardRepository.findById(Mockito.anyInt())).willReturn(Optional.of(w));
        given(this.artifactService.findById(Mockito.anyString())).willThrow(new ArtifactNotFoundException("1234"));


        // When
        System.out.println("Before artifact size: " + w.getArtifacts().size());
        assertThrows(ArtifactNotFoundException.class, () -> {
            Wizard returnedWizard = this.wizardService.addArtifactsToWizard("1234", artifactIds);
            System.out.println("After artifact size: " + returnedWizard.getArtifacts().size());
        }); 
        
        // Then
        verify(this.wizardRepository, times(1)).findById(Mockito.anyInt());
        verify(this.artifactService, times(1)).findById(Mockito.anyString());
        verify(this.wizardRepository, times(0)).save(Mockito.any(Wizard.class));

    }

    @Test
    void testAddArtifactsToWizardNotFoundWizard(){
        // Given
        w.setId(1234);
        List<String> artifactIds = Arrays.asList("123", "456");


        given(this.wizardRepository.findById(Mockito.anyInt())).willThrow(new WizardNotFoundException("1234"));

        // When
        System.out.println("Before artifact size: " + w.getArtifacts().size());
        assertThrows(WizardNotFoundException.class, () -> {
            Wizard returnedWizard = this.wizardService.addArtifactsToWizard("1234", artifactIds);
            System.out.println("After artifact size: " + returnedWizard.getArtifacts().size());
        }); 
        
        // Then
        verify(this.wizardRepository, times(1)).findById(Mockito.anyInt());
        verify(this.artifactService, times(0)).findById(Mockito.anyString());
        verify(this.wizardRepository, times(0)).save(Mockito.any(Wizard.class));
    }


}
