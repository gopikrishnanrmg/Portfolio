package com.portfolio.portfolio_service.skill.exceptions;

public class InvalidSkillUpdateException extends RuntimeException {
    public InvalidSkillUpdateException(String message) {
        super(message);
    }

    public InvalidSkillUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}