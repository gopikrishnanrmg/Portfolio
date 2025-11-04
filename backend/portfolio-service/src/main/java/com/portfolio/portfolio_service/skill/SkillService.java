package com.portfolio.portfolio_service.skill;

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

    public ResponseSkillDTO createSkill(RequestSkillDTO requestSkillDTO, MultipartFile file) {
        try {
            String resourceURI = storageService.upload(file.getBytes());
            Skill skill = new Skill();
            skill.setCategory(requestSkillDTO.category());
            skill.setName(requestSkillDTO.name());
            skill.setUri(resourceURI);
            Skill saved = skillRepository.save(skill);
            return new ResponseSkillDTO(saved.getSkillId(), saved.getCategory(), saved.getName(), file.getBytes());
        } catch (IOException e) {
            throw new FileProcessingException("Failed to process uploaded file", e);
        }
    }

    public List<ResponseSkillDTO> getAllSkills() {
        return skillRepository
                .findAll()
                .stream()
                .filter(skill -> !skill.getIsDeleted())
                .map(skill -> skillMapper.skillToResponseSkillDTO(skill, storageService.download(skill.getUri())))
                .toList();
    }

    public List<ResponseSkillDTO> getSkillsByCategory(String category) {
        return skillRepository
                .findByCategory()
                .stream()
                .filter(skill -> !skill.getIsDeleted())
                .map(skill -> skillMapper.skillToResponseSkillDTO(skill, storageService.download(skill.getUri())))
                .toList();
    }

    public ResponseSkillDTO updateSkill(UUID skillId, RequestSkillDTO requestSkillDTO, MultipartFile file) {
        try {
            Skill skill = skillRepository
                    .findById(skillId)
                    .orElseThrow(() -> new SkillNotFoundException("Skill not found with id: " + skillId));
            skill.setCategory(requestSkillDTO.category());
            skill.setName(requestSkillDTO.name());
            skill.setUri(storageService.upload(file.getBytes()));

            Skill updated = skillRepository.save(skill);

            return new ResponseSkillDTO(
                    updated.getSkillId(),
                    updated.getCategory(),
                    updated.getName(),
                    storageService.download(updated.getUri()));

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
