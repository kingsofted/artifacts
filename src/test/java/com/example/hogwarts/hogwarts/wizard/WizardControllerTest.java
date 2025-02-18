package com.example.hogwarts.hogwarts.wizard;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.print.attribute.standard.Media;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.example.hogwarts.hogwarts.artifact.Artifact;
import com.example.hogwarts.hogwarts.artifact.dto.ArtifactDto;
import com.example.hogwarts.hogwarts.system.StatusCode;
import com.example.hogwarts.hogwarts.wizard.converter.WizardToWizardDtoConverter;
import com.example.hogwarts.hogwarts.wizard.dto.WizardDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

@WebMvcTest(WizardController.class)
public class WizardControllerTest {

    @MockitoBean
    private WizardService wizardService;

    @Autowired
    WizardToWizardDtoConverter wizardToWizardConverter;

    @Autowired
    MockMvc mockMvc;

    WizardDto wDto;
    Wizard w;
    Artifact a;
    List<Wizard> wizardList;

    @BeforeEach
    void setUp() {
        this.wizardList = new ArrayList<>();

        w = new Wizard();
        // w.setId(1234);
        w.setName("Wizard King");

        Artifact a = new Artifact();
        a.setId("1250808601744904191");
        a.setName("Deluminator");
        a.setDescription(
                "A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a.setImageUrl("ImageUrl");

        w.addArtifact(a);
        this.wizardList.add(w);
        wDto = wizardToWizardConverter.convert(w);
    }

    @AfterEach
    void setDown() {

    }

    @Test
    void testFindWizardByIdSuccess() throws JsonProcessingException, Exception {
        // Given
        given(this.wizardService.findWizardbyId(Mockito.anyString())).willReturn(w);

        // When @ Then
        this.mockMvc
                .perform(get("/api/v1/wizard/1250808601744904196").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value("Wizard King"));

        // Then
        verify(this.wizardService, times(1)).findWizardbyId(Mockito.anyString());
    }

    @Test
    void testFindWizardByIdNotFound() throws JsonProcessingException, Exception {
        // Given
        given(this.wizardService.findWizardbyId(Mockito.anyString())).willReturn(w);

        // When @ Then
        this.mockMvc
                .perform(get("/api/v1/wizard/1250808601744904196").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value("Wizard King"));

        // Then
        verify(this.wizardService, times(1)).findWizardbyId(Mockito.anyString());
    }

    @Test
    void testFindAllWizardSuccess() throws Exception {
        // Given
        given(this.wizardService.findAllWizards()).willReturn(wizardList);

        // When & Then
        this.mockMvc.perform(get("/api/v1/wizard"))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.data[0].artifacts").isNotEmpty())
                .andExpect(jsonPath("$.data[0].artifacts[0].name").value("Deluminator"))
                .andExpect(jsonPath("$.data[0].name").value("Wizard King"));

        // Then
        verify(this.wizardService, times(1)).findAllWizards();
    }

    @Test
    void testFindAllWizardFailure() throws Exception {
        // Given
        given(this.wizardService.findAllWizards()).willReturn(null);

        // When & Then
        this.mockMvc.perform(get("/api/v1/wizard"))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty());

        // Then
        verify(this.wizardService, times(1)).findAllWizards();
    }

    @Test
    void testAddWizardSuccess() throws JsonProcessingException, Exception {
        // Given
        given(this.wizardService.addWizard(Mockito.any(Wizard.class))).willReturn(w);

        // When & Then
        this.mockMvc
                .perform(post("/api/v1/wizard").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(wDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value("Wizard King"));

        // Verify
        verify(this.wizardService, times(1)).addWizard(Mockito.any(Wizard.class));

    }

    @Test
    void testAddWizardFailure() throws JsonProcessingException, Exception {
        // Given
        given(this.wizardService.addWizard(Mockito.any(Wizard.class))).willThrow(
                new WizardExistedException(wDto.name()));

        // When & Then
        this.mockMvc
                .perform(post("/api/v1/wizard").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(wDto)))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.FORBIDDEN))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.data.name").doesNotExist());

        // Verify
        verify(this.wizardService, times(1)).addWizard(Mockito.any(Wizard.class));
    }

    @Test
    void testAddWizardNotValid() throws JsonProcessingException, Exception {
        // Given
        WizardDto newWizardDto = new WizardDto(null, "", 3);
        // When & Then
        this.mockMvc
                .perform(post("/api/v1/wizard").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(newWizardDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details"))
                .andExpect(jsonPath("$.data.name").value("Name field cannot be empty"));

        // Verify
        verify(this.wizardService, times(0)).addWizard(Mockito.any(Wizard.class));
    }

    @Test
    void testUpdateWizardSuccess() throws JsonProcessingException, Exception{
        // Given
        WizardDto updatedWizardDto = new WizardDto(null, "Update wizard", null);

        w.setName("Updated wizard");
        given(this.wizardService.updateWizard(Mockito.anyString(), Mockito.any(Wizard.class))).willReturn(w);

        // When & Then
        this.mockMvc
            .perform(put("/api/v1/wizard/123").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(updatedWizardDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
            .andExpect(jsonPath("$.message").value("Updated Successfully"))
            .andExpect(jsonPath("$.data.name").value("Updated wizard"))
            .andExpect(jsonPath("$.data.artifacts[0].name").value("Deluminator"));

        // Verify
        verify(this.wizardService, times(1)).updateWizard(Mockito.anyString(), Mockito.any(Wizard.class));

    }

    @Test
    void testUpdateWizardNotFound() throws JsonProcessingException, Exception{
        // Given
        w.setId(1234);
        given(this.wizardService.updateWizard( Mockito.anyString(),Mockito.any(Wizard.class))).willThrow(new WizardNotFoundException(String.valueOf(w.getId())));

        // When & Then
        this.mockMvc.perform(put("/api/v1/wizard/123").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(wDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with Id 1234 :("));

        // Verify
        verify(this.wizardService, times(1)).updateWizard(Mockito.anyString(), Mockito.any(Wizard.class));
    }


    @Test
    void testUpdateWizardNotValid() throws JsonProcessingException, Exception{
        // Given
        WizardDto wizardDto = new WizardDto(null,"", 0);

        // When & then
        ResultActions result = this.mockMvc.perform(put("/api/v1/wizard/123").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(wizardDto)))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.data").isNotEmpty());
        
        System.out.println("MVC: " + result.andDo(print()));
    }

    @Test
    void testDeleteWizardSuccess() throws JsonProcessingException, Exception{
        // Given
        w.setId(1234);
        given(this.wizardService.deleteWizard(Mockito.anyString())).willReturn(w);
        
        // When & then
        ResultActions result = this.mockMvc.perform(delete("/api/v1/wizard/123").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value("Wizard King"));
        
        System.out.println("MVC: " + result.andDo(print()));
        verify(this.wizardService, times(1)).deleteWizard(Mockito.anyString());
    }

    @Test
    void testDeleteWizardNotFound() throws Exception{
        // Given
        w.setId(1234);
        given(this.wizardService.deleteWizard(Mockito.anyString())).willThrow(new WizardNotFoundException("1234"));
        
        // When & then
        ResultActions result = this.mockMvc.perform(delete("/api/v1/wizard/123").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.data.artifacts").doesNotExist());
        
        System.out.println("MVC: " + result.andDo(print()));
        verify(this.wizardService, times(1)).deleteWizard(Mockito.anyString());
    }

    @Test
    void testAddArtifactToWizardSuccess() throws Exception{
        // Given
        w.setId(1234);
        List<String> artifactIds = Arrays.asList("123", "456");
        given(this.wizardService.addArtifactsToWizard(Mockito.anyString(), Mockito.<String>anyList())).willReturn(w);
        
        // When & then
        ResultActions result = this.mockMvc.perform(post("/api/v1/wizard/123").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(artifactIds)))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value("Wizard King"));
        
        System.out.println("MVC: " + result.andDo(print()));
        verify(this.wizardService, times(1)).addArtifactsToWizard((Mockito.anyString()), Mockito.<String>anyList());
    }


}
