package com.portfolio.portfolio_service.projects.exceptions;

public class DuplicateProjectException extends RuntimeException {
    public DuplicateProjectException(String message) {
        super(message);
    }

    public DuplicateProjectException(String message, Throwable cause) {
        super(message, cause);
    }
}
