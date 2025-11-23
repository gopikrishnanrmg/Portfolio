package com.portfolio.portfolio_service.workexp.exceptions;

public class WorkExpNotFoundException extends RuntimeException {
    public WorkExpNotFoundException(String message) {
        super(message);
    }

    public WorkExpNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}