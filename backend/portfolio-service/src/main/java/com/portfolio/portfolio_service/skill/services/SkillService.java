package com.portfolio.portfolio_service.skill.services;

import com.portfolio.portfolio_service.skill.dtos.SkillRequest;
import com.portfolio.portfolio_service.skill.dtos.SkillResponse;
import com.portfolio.portfolio_service.skill.mappers.SkillMapper;
import com.portfolio.portfolio_service.skill.models.Category;
import com.portfolio.portfolio_service.blob_storage.StorageService;
import com.portfolio.portfolio_service.skill.exceptions.FileProcessingException;
import com.portfolio.portfolio_service.skill.exceptions.SkillNotFoundException;
import com.portfolio.portfolio_service.skill.models.Skill;
import com.portfolio.portfolio_service.skill.repositories.SkillRepository;
import com.portfolio.portfolio_service.blob_storage.dtos.StorageResult;
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
            StorageResult resource = storageService.upload(file.getBytes());
            Skill skill = skillMapper.skillRequestToSkill(skillRequest, resource.key());
            Skill saved = skillRepository.save(skill);

            String iconUrl = storageService.generatePresignedUrl(resource.key());
            return skillMapper.skillToSkillResponse(saved, iconUrl);
        } catch (IOException e) {
            throw new FileProcessingException("Failed to process uploaded file", e);
        }
    }

    public List<SkillResponse> getAllSkills() {
        return skillRepository.findAll()
                .stream()
                .filter(skill -> !skill.getIsDeleted())
                .map(skill -> {
                    String iconUrl = skill.getStorageKey() != null
                            ? storageService.generatePresignedUrl(skill.getStorageKey())
                            : null;
                    return skillMapper.skillToSkillResponse(skill, iconUrl);
                })
                .toList();
    }

    public List<SkillResponse> getSkillsByCategory(Category category) {
        return skillRepository.findByCategory(category)
                .stream()
                .filter(skill -> !skill.getIsDeleted())
                .map(skill -> {
                    String iconUrl = skill.getStorageKey() != null
                            ? storageService.generatePresignedUrl(skill.getStorageKey())
                            : null;
                    return skillMapper.skillToSkillResponse(skill, iconUrl);
                })
                .toList();
    }

    public SkillResponse updateSkill(UUID skillId, SkillRequest skillRequest) {
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new SkillNotFoundException("Skill not found with id: " + skillId));

        skill.setCategory(skillRequest.category());
        skill.setName(skillRequest.name());

        Skill updated = skillRepository.save(skill);
        String iconUrl = updated.getStorageKey() != null
                ? storageService.generatePresignedUrl(updated.getStorageKey())
                : null;

        return skillMapper.skillToSkillResponse(updated, iconUrl);
    }

    public SkillResponse uploadSkillFile(UUID skillId, MultipartFile file) {
        try {
            Skill skill = skillRepository.findById(skillId)
                    .orElseThrow(() -> new SkillNotFoundException("Skill not found with id: " + skillId));

            if (file != null && !file.isEmpty()) {
                if (skill.getStorageKey() != null)
                    storageService.delete(skill.getStorageKey());

                StorageResult resource = storageService.upload(file.getBytes());
                skill.setStorageKey(resource.key());
            }

            Skill updated = skillRepository.save(skill);
            String iconUrl = updated.getStorageKey() != null
                    ? storageService.generatePresignedUrl(updated.getStorageKey())
                    : null;

            return skillMapper.skillToSkillResponse(updated, iconUrl);

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
        skillRepository.findById(skillId).ifPresent(skill -> {
            if (skill.getStorageKey() != null) {
                storageService.delete(skill.getStorageKey());
            }
            skillRepository.delete(skill);
        });
    }

}
