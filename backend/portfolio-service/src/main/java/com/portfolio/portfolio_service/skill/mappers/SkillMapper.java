package com.portfolio.portfolio_service.skill.mappers;

import com.portfolio.portfolio_service.skill.dtos.SkillResponse;
import com.portfolio.portfolio_service.skill.models.Skill;
import org.springframework.stereotype.Component;

@Component
public class SkillMapper {
    public SkillResponse skillToSkillResponse(Skill skill, byte[] resource) {
        return new SkillResponse(skill.getSkillId(), skill.getCategory(), skill.getName(), resource);
    }
}
