package com.portfolio.portfolio_service.skill;

import org.springframework.stereotype.Component;

@Component
public class SkillMapper {
    public ResponseSkillDTO skillToResponseSkillDTO(Skill skill, byte[] resource) {
        return new ResponseSkillDTO(skill.getSkillId(), skill.getCategory(), skill.getName(), resource);
    }
}
