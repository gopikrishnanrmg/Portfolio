package com.portfolio.portfolio_service.skill.exceptions;

public class SkillNotFoundException extends RuntimeException {
    public SkillNotFoundException(String message) {
        super(message);
    }

    public SkillNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
   }
