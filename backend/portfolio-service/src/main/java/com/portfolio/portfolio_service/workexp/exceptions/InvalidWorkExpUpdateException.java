package com.portfolio.portfolio_service.workexp.exceptions;

public class InvalidWorkExpUpdateException extends RuntimeException {
    public InvalidWorkExpUpdateException(String message) {
        super(message);
    }

    public InvalidWorkExpUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}