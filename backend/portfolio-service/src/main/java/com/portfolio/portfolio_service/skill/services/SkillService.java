package com.portfolio.portfolio_service.skill.services;

import com.portfolio.portfolio_service.skill.dtos.CreateSkillRequest;
import com.portfolio.portfolio_service.skill.dtos.SkillResponse;
import com.portfolio.portfolio_service.skill.dtos.UpdateSkillRequest;
import com.portfolio.portfolio_service.skill.exceptions.DuplicateSkillException;
import com.portfolio.portfolio_service.skill.exceptions.InvalidSkillUpdateException;
import com.portfolio.portfolio_service.skill.mappers.SkillMapper;
import com.portfolio.portfolio_service.skill.models.Category;
import com.portfolio.portfolio_service.blob_storage.StorageService;
import com.portfolio.portfolio_service.exceptions.FileProcessingException;
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

    public SkillResponse createSkill(CreateSkillRequest skillRequest, MultipartFile file) {
        if (skillRepository.existsByNameAndIsDeletedFalse(skillRequest.name()))
            throw new DuplicateSkillException("Skill name already exists");

        try {
            StorageResult resource = storageService.upload(file.getBytes());
            Skill skill = skillMapper.skillRequestToSkill(skillRequest, resource.key());
            Skill saved = skillRepository.save(skill);

            String url = storageService.generatePresignedUrl(resource.key());
            return skillMapper.skillToSkillResponse(saved, url);
        } catch (IOException e) {
            throw new FileProcessingException("Failed to process uploaded file", e);
        }
    }

    public List<SkillResponse> getAllSkills() {
        return skillRepository.findAll()
                .stream()
                .filter(skill -> !skill.getIsDeleted())
                .map(skill -> skillMapper.skillToSkillResponse(skill, storageService.generatePresignedUrl(skill.getStorageKey())))
                .toList();
    }

    public List<SkillResponse> getSkillsByCategory(Category category) {
        return skillRepository.findByCategory(category)
                .stream()
                .filter(skill -> !skill.getIsDeleted())
                .map(skill -> skillMapper.skillToSkillResponse(skill, storageService.generatePresignedUrl(skill.getStorageKey())))
                .toList();
    }

    public SkillResponse updateSkill(UUID skillId, UpdateSkillRequest skillRequest) {
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new SkillNotFoundException("Skill not found with id: " + skillId));

        if (skillRequest.category() != null)
            skill.setCategory(skillRequest.category());

        if (skillRequest.name() != null) {
            if(!skillRequest.name().isBlank())
                skill.setName(skillRequest.name());
            else
                throw new InvalidSkillUpdateException("Name cannot be blank");
        }

        Skill updated = skillRepository.save(skill);

        return skillMapper.skillToSkillResponse(updated, storageService.generatePresignedUrl(updated.getStorageKey()));
    }

    public SkillResponse uploadSkillFile(UUID skillId, MultipartFile file) {
        try {
            Skill skill = skillRepository.findById(skillId)
                    .orElseThrow(() -> new SkillNotFoundException("Skill not found with id: " + skillId));

            storageService.delete(skill.getStorageKey());

            StorageResult resource = storageService.upload(file.getBytes());
            skill.setStorageKey(resource.key());

            Skill updated = skillRepository.save(skill);

            return skillMapper.skillToSkillResponse(updated, storageService.generatePresignedUrl(updated.getStorageKey()));

        } catch (IOException e) {
            throw new FileProcessingException("Failed to process uploaded file", e);
        }
    }

    public void deleteSkill(UUID skillId) {
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new SkillNotFoundException("Skill not found with id: " + skillId));

        skill.setIsDeleted(true);
        skillRepository.save(skill);
    }

    public void deleteSkillHard(UUID skillId) {
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new SkillNotFoundException("Skill not found with id: " + skillId));

        storageService.delete(skill.getStorageKey());

        skillRepository.delete(skill);
    }
}
