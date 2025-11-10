package com.portfolio.portfolio_service.skill.exceptions;

public class DuplicateSkillException extends RuntimeException {
    public DuplicateSkillException(String message) {
        super(message);
    }

    public DuplicateSkillException(String message, Throwable cause) {
        super(message, cause);
    }
}
