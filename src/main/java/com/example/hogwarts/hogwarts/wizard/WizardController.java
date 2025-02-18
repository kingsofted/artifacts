package com.example.hogwarts.hogwarts.wizard;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hogwarts.hogwarts.aop.AutoFill;
import com.example.hogwarts.hogwarts.aop.OperationType;
import com.example.hogwarts.hogwarts.artifact.Artifact;
import com.example.hogwarts.hogwarts.artifact.converter.ArtifactDtoToArtifactConverter;
import com.example.hogwarts.hogwarts.artifact.dto.ArtifactDto;
import com.example.hogwarts.hogwarts.system.Result;
import com.example.hogwarts.hogwarts.system.StatusCode;
import com.example.hogwarts.hogwarts.wizard.converter.WizardDtoToWizardConverter;
import com.example.hogwarts.hogwarts.wizard.converter.WizardToWizardDtoConverter;
import com.example.hogwarts.hogwarts.wizard.dto.WizardDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.endpoint.base-url}/wizard")
public class WizardController {

    private final WizardService wizardService;
    private final WizardDtoToWizardConverter wizardConverter;
    private final WizardToWizardDtoConverter wizardDtoConverter;
    private final ArtifactDtoToArtifactConverter artifactConverter;

    @GetMapping("/{wizardId}")
    public Result findWizardById(@PathVariable String wizardId){
        Wizard wizard = wizardService.findWizardbyId(wizardId);

        return new Result(true, StatusCode.SUCCESS, "Find One Success", wizard);
    }

    @GetMapping
    @AutoFill(OperationType.MORNING)
    public Result findAllWizard(){
        List<Wizard> wizardList = wizardService.findAllWizards();
        return new Result(true, StatusCode.SUCCESS, "Find One Success", wizardList);
    }

    @PostMapping
    public Result addWizard(@Valid @RequestBody WizardDto wizardDto){
        // Wizard wizard = new Wizard();
        // BeanUtils.copyProperties(wizardDto, wizard);
    
        Wizard wizard = this.wizardService.addWizard(wizardConverter.convert(wizardDto));
        WizardDto returnDto = wizardDtoConverter.convert(wizard);
        return new Result(true, StatusCode.SUCCESS, "Added Successfully", returnDto);
    }

    @PutMapping("/{wizardId}")
    public Result updateWizard(@PathVariable String wizardId, @Valid @RequestBody WizardDto wizardDto){
        Wizard wizard = this.wizardService.updateWizard(wizardId ,wizardConverter.convert(wizardDto));
        WizardDto returnDto = wizardDtoConverter.convert(wizard);
        return new Result(true, StatusCode.SUCCESS, "Updated Successfully", wizard);
    }

    @DeleteMapping("/{wizardId}")
    public Result deleteWizard(@PathVariable String wizardId){
        Wizard wizard = this.wizardService.deleteWizard(wizardId);

        return new Result(true, StatusCode.SUCCESS, "Delete Successfully", wizard);
    }

    @PostMapping("/{wizardId}")
    public Result addArtifactsToWizard(@PathVariable String wizardId, @Valid @RequestBody List<String> artifactIds){
        Wizard wizard = this.wizardService.addArtifactsToWizard(wizardId, artifactIds);

        return new Result(true, StatusCode.SUCCESS, "Added Artifacts to Wizard successfully", wizard);
    }
    

}
