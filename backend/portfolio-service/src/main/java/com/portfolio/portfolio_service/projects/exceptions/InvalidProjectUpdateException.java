package com.portfolio.portfolio_service.projects.exceptions;

public class InvalidProjectUpdateException extends RuntimeException {
    public InvalidProjectUpdateException(String message) {
        super(message);
    }

    public InvalidProjectUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}