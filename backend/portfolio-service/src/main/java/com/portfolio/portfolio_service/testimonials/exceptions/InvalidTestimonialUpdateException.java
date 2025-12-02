package com.portfolio.portfolio_service.testimonials.exceptions;

public class InvalidTestimonialUpdateException extends RuntimeException {
    public InvalidTestimonialUpdateException(String message) {
        super(message);
    }

    public InvalidTestimonialUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}