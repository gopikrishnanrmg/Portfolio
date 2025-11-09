package com.portfolio.portfolio_service.skill.controllers;

import com.portfolio.portfolio_service.skill.dtos.CreateSkillRequest;
import com.portfolio.portfolio_service.skill.dtos.SkillResponse;
import com.portfolio.portfolio_service.skill.dtos.UpdateSkillRequest;
import com.portfolio.portfolio_service.skill.models.Category;
import com.portfolio.portfolio_service.skill.services.SkillService;
import com.portfolio.portfolio_service.validators.ValidFile;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    @PostMapping()
    public ResponseEntity<SkillResponse> createSkill(
            @Valid @RequestPart("data") CreateSkillRequest skillRequest,
            @ValidFile @RequestPart("file") MultipartFile file){
        SkillResponse created = skillService.createSkill(skillRequest, file);
        URI location = URI.create("/api/v1/skills/" + created.skillId());
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping()
    public ResponseEntity<List<SkillResponse>> getAllSkills(){
        return ResponseEntity.ok().body(skillService.getAllSkills());
    }

    @GetMapping("/{category}")
    public ResponseEntity<List<SkillResponse>> getSkillsByCategory(@PathVariable String category) {
        Category enumCategory = Category.valueOf(category.toUpperCase());
        return ResponseEntity.ok(skillService.getSkillsByCategory(enumCategory));
    }


    @PutMapping("/{id}")
    public ResponseEntity<SkillResponse> updateSkill(
            @RequestBody UpdateSkillRequest skillRequest,
            @PathVariable UUID id) {
        return ResponseEntity.ok(skillService.updateSkill(id, skillRequest));
    }

    @PutMapping("/{id}/file")
    public ResponseEntity<SkillResponse> uploadSkillFile(
            @ValidFile @RequestParam MultipartFile file,
            @PathVariable UUID id) {
        return ResponseEntity.ok(skillService.uploadSkillFile(id, file));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable UUID id){
        return skillService.deleteSkill(id)?
                ResponseEntity.noContent().build():ResponseEntity.notFound().build();
    }

    @DeleteMapping("/hard/{id}")
    public ResponseEntity<Void> deleteByIdHard(@PathVariable UUID id) {
        try {
            skillService.deleteSkillHard(id);
            return ResponseEntity.noContent().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
