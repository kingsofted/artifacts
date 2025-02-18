package com.example.hogwarts.hogwarts.artifact;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import javax.print.attribute.standard.Media;

import org.hamcrest.Matchers;
import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.example.hogwarts.hogwarts.artifact.converter.ArtifactToArtifactDtoConverter;
import com.example.hogwarts.hogwarts.artifact.dto.ArtifactDto;
import com.example.hogwarts.hogwarts.system.StatusCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ArtifactController.class)
// @AutoConfigureMockMvc
public class ArtifactControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Captor
    private ArgumentCaptor<Artifact> artifactCaptor;

    @MockitoBean
    ArtifactService artifactService;

    List<Artifact> artifacts;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

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

        Artifact a3 = new Artifact();
        a3.setId("1250808601744904193");
        a3.setName("Elder Wand");
        a3.setDescription(
                "The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.");
        a3.setImageUrl("ImageUrl");
        this.artifacts.add(a3);

        Artifact a4 = new Artifact();
        a4.setId("1250808601744904194");
        a4.setName("The Marauder's Map");
        a4.setDescription(
                "A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.");
        a4.setImageUrl("ImageUrl");
        this.artifacts.add(a4);

        Artifact a5 = new Artifact();
        a5.setId("1250808601744904195");
        a5.setName("The Sword Of Gryffindor");
        a5.setDescription(
                "A goblin-made sword adorned with large rubies on the pommel. It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts.");
        a5.setImageUrl("ImageUrl");
        this.artifacts.add(a5);

        Artifact a6 = new Artifact();
        a6.setId("1250808601744904196");
        a6.setName("Resurrection Stone");
        a6.setDescription(
                "The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them.");
        a6.setImageUrl("ImageUrl");
        this.artifacts.add(a6);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void testFindArtifactByIdSuccess() throws Exception {
        // Given
        given(this.artifactService.findById("1250808601744904196")).willReturn(this.artifacts.get(5));

        // When and then
        this.mockMvc
                .perform(get(this.baseUrl + "/artifacts/1250808601744904196")
                        .accept(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value("1250808601744904196"))
                .andExpect(jsonPath("$.data.name").value("Resurrection Stone"))
                .andExpect(jsonPath("$.data.description").value(
                        "The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them."));
    }

    @Test
    void testFindArtifactByIdNotFound() throws Exception {
        given(this.artifactService.findById(Mockito.any(String.class)))
                .willThrow(new ArtifactNotFoundException("1250808601744904196"));

        this.mockMvc.perform(get(this.baseUrl + "/artifacts/1250808601744904196"))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with Id 1250808601744904196 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testFindAllArtifactsSuccess() throws Exception {
        // Given
        given(this.artifactService.findAll()).willReturn(this.artifacts);

        // When and then
        this.mockMvc.perform(get(this.baseUrl + "/artifacts").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data[0].id").value("1250808601744904191"))
                .andExpect(jsonPath("$.data[0].name").value("Deluminator"))
                .andExpect(jsonPath("$.data[1].id").value("1250808601744904192"))
                .andExpect(jsonPath("$.data[1].name").value("Invisibility Cloak"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.artifacts.size())));
    }

    @Test
    void testAddArtifactSuccess() throws JsonProcessingException, Exception {
        // Given
        ArtifactDto artifactDto = new ArtifactDto(null, "Artifact 3", "Description...", "ImageUrl...", null);

        Artifact saveArtifact = new Artifact();
        saveArtifact.setId("2334");
        saveArtifact.setName("Artifact 3");
        saveArtifact.setDescription("Description...");
        saveArtifact.setImageUrl("ImageUrl...");
        given(this.artifactService.save(Mockito.any(Artifact.class))).willReturn(saveArtifact);

        // When and then
        this.mockMvc
                .perform(post(this.baseUrl + "/artifacts").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(artifactDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Added successfully"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(saveArtifact.getName()))
                .andExpect(jsonPath("$.data.description").value(saveArtifact.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(saveArtifact.getImageUrl()))
                .andExpect(jsonPath("$.data.wizard").doesNotExist());


        // Then - verify artifactService.save(artifactCapture.capture()) is called once, then get the values that passed to the service and then do comparison
        verify(artifactService, times(1)).save(artifactCaptor.capture());
        Artifact capturedArtifact = artifactCaptor.getValue();

        // Validate the mapping from ArtifactDto -> Artifact
        assertThat(capturedArtifact.getName()).isEqualTo(artifactDto.name());
        assertThat(capturedArtifact.getDescription()).isEqualTo(artifactDto.description());
        assertThat(capturedArtifact.getImageUrl()).isEqualTo(artifactDto.imageUrl());
        assertThat(capturedArtifact.getId()).isNull(); 
    }

    @Test
    void testAddArtifactFailure() throws JsonProcessingException, Exception {
        // ❌ Given: An invalid DTO (null name)
        ArtifactDto invalidArtifactDto = new ArtifactDto(null, "Test", "", "ImageUrl...", null);

        // When & Then: Perform the request and expect validation error (400 Bad Request)
        mockMvc.perform(post(this.baseUrl + "/artifacts")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(invalidArtifactDto)))
                .andExpect(status().isBadRequest()) // Expect HTTP 400
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details")) // Customize based on your exception handler
                .andExpect(jsonPath("$.data").exists()); // Expect errors in response
    }

    @Test
    void testupdateArtifactSuccess() throws JsonProcessingException, Exception{
        // Given
        ArtifactDto updateArtifactDto = new ArtifactDto(null, "Test", "des", "ImageUrl...", null);
        Artifact updateArtifact = new Artifact("12345", "Updated", "Updated description", "Updated image url", null);
        given(this.artifactService.update(Mockito.anyString(), Mockito.any(Artifact.class))).willReturn(updateArtifact);

        // When
        this.mockMvc.perform(put(this.baseUrl + "/artifacts/12345")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updateArtifactDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.id").value("12345"))
                .andExpect(jsonPath("$.data.name").value("Updated"));

        // Then
        verify(this.artifactService, times(1)).update(Mockito.anyString(), Mockito.any(Artifact.class));

    }

    @Test
    void testupdateArtifactIdNotFound() throws JsonProcessingException, Exception{
        // Given
        ArtifactDto updateArtifactDto = new ArtifactDto(null, "name", "des", "ImageUrl...", null);
        given(this.artifactService.update(Mockito.anyString(), Mockito.any(Artifact.class))).willThrow(new ArtifactNotFoundException("12345"));

        // When
        this.mockMvc.perform(put(this.baseUrl + "/artifacts/{artifactId}", "12456").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(updateArtifactDto)))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with Id 12345 :(")) // Customize based on your exception handler
                .andExpect(jsonPath("$.data").isEmpty()); // Empty

        // Then
        verify(this.artifactService, times(1)).update(Mockito.anyString(), Mockito.any(Artifact.class));
    }

    @Test
    void testupdateArtifactFailure() throws JsonProcessingException, Exception{
        // Given
        ArtifactDto updateArtifactDto = new ArtifactDto(null, "", "des", "ImageUrl...", null);

        // When
        this.mockMvc.perform(put(this.baseUrl + "/artifacts/{artifactId}", "12456").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(updateArtifactDto)))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details")) // Customize based on your exception handler
                .andExpect(jsonPath("$.data").exists()); // Expect errors in response

        // Then this class wont be called
        verify(this.artifactService, times(0)).update(Mockito.anyString(), Mockito.any(Artifact.class));
    }

    @Test
    void testDeleteArtifactSuccess() throws Exception{
        // Given
        doNothing().when(this.artifactService).delete(Mockito.anyString());

        // When 
        this.mockMvc.perform(delete(this.baseUrl + "/artifacts/12345").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Deleted Successfully")) // Customize based on your exception handler
                .andExpect(jsonPath("$.data").doesNotExist()); // Expect errors in response

        // Then
        verify(this.artifactService, times(1)).delete(Mockito.anyString());
    }

    @Test
    void testDeleteArtifactErrorWithNonExistentId() throws Exception{
        // Given
        doThrow(new ArtifactNotFoundException("1234")).when(this.artifactService).delete(Mockito.anyString());

        // When 
        this.mockMvc.perform(delete(this.baseUrl + "/artifacts/12345").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with Id 1234 :(")) // Customize based on your exception handler
                .andExpect(jsonPath("$.data").doesNotExist()); // Expect errors in response

        // Then
        verify(this.artifactService, times(1)).delete(Mockito.anyString());
    }


}
