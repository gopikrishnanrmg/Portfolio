package com.portfolio.portfolio_service.workexp.exceptions;

public class DuplicateWorkExpException extends RuntimeException {
    public DuplicateWorkExpException(String message) {
        super(message);
    }

    public DuplicateWorkExpException(String message, Throwable cause) {
        super(message, cause);
    }
}