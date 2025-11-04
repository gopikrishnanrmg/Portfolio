package com.portfolio.portfolio_service.skill;

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
    public ResponseEntity<ResponseSkillDTO> createSkill(
            @RequestPart("data") RequestSkillDTO requestSkillDTO,
            @RequestPart("file") MultipartFile file){
        ResponseSkillDTO created = skillService.createSkill(requestSkillDTO, file);
        URI location = URI.create("/api/v1/skills/" + created.skillId());
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping()
    public ResponseEntity<List<ResponseSkillDTO>> getAllSkills(){
        return ResponseEntity.ok().body(skillService.getAllSkills());
    }

    @GetMapping("/{category}")
    public ResponseEntity<List<ResponseSkillDTO>> getSkillsByCategory(@PathVariable String category){
        return ResponseEntity.ok().body(skillService.getSkillsByCategory(category));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ResponseSkillDTO> updateSkill(
            @RequestPart("requestSkillDTO") RequestSkillDTO requestSkillDTO,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @PathVariable UUID id) {

        ResponseSkillDTO response = skillService.updateSkill(id, requestSkillDTO, file);
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
