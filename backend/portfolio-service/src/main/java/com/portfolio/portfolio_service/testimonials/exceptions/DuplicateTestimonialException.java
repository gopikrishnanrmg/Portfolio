package com.portfolio.portfolio_service.testimonials.exceptions;

public class DuplicateTestimonialException extends RuntimeException {
    public DuplicateTestimonialException(String message) {
        super(message);
    }

    public DuplicateTestimonialException(String message, Throwable cause) {
        super(message, cause);
    }
}
