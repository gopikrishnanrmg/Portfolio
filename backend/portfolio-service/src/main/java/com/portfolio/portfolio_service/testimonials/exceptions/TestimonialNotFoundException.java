package com.portfolio.portfolio_service.testimonials.exceptions;

public class TestimonialNotFoundException extends RuntimeException {
    public TestimonialNotFoundException(String message) {
        super(message);
    }

    public TestimonialNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
