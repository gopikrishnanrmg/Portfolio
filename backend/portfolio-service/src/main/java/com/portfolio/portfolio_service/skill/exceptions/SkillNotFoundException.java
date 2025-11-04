package com.portfolio.portfolio_service.skill.exceptions;

public class SkillNotFoundException extends RuntimeException {

    public SkillNotFoundException(String skillName) {
        super("Skill not found: " + skillName);
    }

    public SkillNotFoundException(String skillName, Throwable cause) {
        super("Skill not found: " + skillName, cause);
    }
}
