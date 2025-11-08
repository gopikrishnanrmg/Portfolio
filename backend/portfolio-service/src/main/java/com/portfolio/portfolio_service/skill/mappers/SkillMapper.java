package com.portfolio.portfolio_service.skill.mappers;

import com.portfolio.portfolio_service.skill.dtos.SkillRequest;
import com.portfolio.portfolio_service.skill.dtos.SkillResponse;
import com.portfolio.portfolio_service.skill.models.Skill;
import org.springframework.stereotype.Component;

@Component
public class SkillMapper {
    public SkillResponse skillToSkillResponse(Skill skill, String iconUrl) {
        return new SkillResponse(skill.getSkillId(), skill.getCategory(), skill.getName(), iconUrl);
    }

    public Skill skillRequestToSkill(SkillRequest skillRequest, String key) {
        return Skill
                .builder()
                .name(skillRequest.name())
                .category(skillRequest.category())
                .storageKey(key)
                .build();
    }
}
