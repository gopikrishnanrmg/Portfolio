package com.portfolio.portfolio_service.skill.controllers;

import com.portfolio.portfolio_service.skill.dtos.SkillRequest;
import com.portfolio.portfolio_service.skill.dtos.SkillResponse;
import com.portfolio.portfolio_service.skill.models.Category;
import com.portfolio.portfolio_service.skill.services.SkillService;
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
            @RequestPart("data") SkillRequest skillRequest,
            @RequestPart("file") MultipartFile file){
        SkillResponse created = skillService.createSkill(skillRequest, file);
        URI location = URI.create("/api/v1/skills/" + created.skillId());
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping()
    public ResponseEntity<List<SkillResponse>> getAllSkills(){
        return ResponseEntity.ok().body(skillService.getAllSkills());
    }

    @GetMapping("/{category}")
    public ResponseEntity<List<SkillResponse>> getSkillsByCategory(@PathVariable Category category){
        return ResponseEntity.ok().body(skillService.getSkillsByCategory(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SkillResponse> updateSkill(
            @RequestPart("requestSkillDTO") SkillRequest skillRequest,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @PathVariable UUID id) {

        SkillResponse response = skillService.updateSkill(id, skillRequest, file);
        return ResponseEntity.ok(response);
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
