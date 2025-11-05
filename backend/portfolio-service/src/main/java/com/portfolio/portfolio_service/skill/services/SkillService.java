package com.portfolio.portfolio_service.skill.services;

import com.portfolio.portfolio_service.skill.dtos.SkillRequest;
import com.portfolio.portfolio_service.skill.dtos.SkillResponse;
import com.portfolio.portfolio_service.skill.mappers.SkillMapper;
import com.portfolio.portfolio_service.skill.models.Category;
import com.portfolio.portfolio_service.skill.storage.StorageService;
import com.portfolio.portfolio_service.skill.exceptions.FileProcessingException;
import com.portfolio.portfolio_service.skill.exceptions.SkillNotFoundException;
import com.portfolio.portfolio_service.skill.models.Skill;
import com.portfolio.portfolio_service.skill.repositories.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;
    private final StorageService storageService;

    public SkillResponse createSkill(SkillRequest skillRequest, MultipartFile file) {
        try {
            String resourceURI = storageService.upload(file.getBytes());
            Skill skill = skillMapper.skillRequestToSkill(skillRequest, resourceURI);
            Skill saved = skillRepository.save(skill);
            return skillMapper.skillToSkillResponse(saved, file.getBytes());
        } catch (IOException e) {
            throw new FileProcessingException("Failed to process uploaded file", e);
        }
    }

    public List<SkillResponse> getAllSkills() {
        return skillRepository
                .findAll()
                .stream()
                .filter(skill -> !skill.getIsDeleted())
                .map(skill -> skillMapper.skillToSkillResponse(skill, storageService.download(skill.getUri())))
                .toList();
    }

    public List<SkillResponse> getSkillsByCategory(Category category) {
        return skillRepository
                .findByCategory(category)
                .stream()
                .filter(skill -> !skill.getIsDeleted())
                .map(skill -> skillMapper.skillToSkillResponse(skill, storageService.download(skill.getUri())))
                .toList();
    }

    public SkillResponse updateSkill(UUID skillId, SkillRequest skillRequest, MultipartFile file) {
        try {
            Skill skill = skillRepository
                    .findById(skillId)
                    .orElseThrow(() -> new SkillNotFoundException("Skill not found with id: " + skillId));
            skill.setCategory(skillRequest.category());
            skill.setName(skillRequest.name());

            if (file != null && !file.isEmpty())
                skill.setUri(storageService.upload(file.getBytes()));

            Skill updated = skillRepository.save(skill);

            return skillMapper.skillToSkillResponse(updated, storageService.download(updated.getUri()));

        } catch (IOException e) {
            throw new FileProcessingException("Failed to process uploaded file", e);
        }
    }

    public boolean deleteSkill(UUID skillId) {
        return skillRepository
                .findById(skillId)
                .map(skill -> {
                    skill.setIsDeleted(true);
                    skillRepository.save(skill);
                    return true;
                })
                .orElse(false);
    }

    public void deleteSkillHard(UUID skillId) {
        skillRepository.deleteById(skillId);
    }

}
