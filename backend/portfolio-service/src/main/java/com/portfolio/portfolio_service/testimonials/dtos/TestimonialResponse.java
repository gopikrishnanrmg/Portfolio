package com.portfolio.portfolio_service.testimonials.dtos;

import java.util.UUID;

public record TestimonialResponse(UUID testimonialId, String name, String role, String text, String initials, String accent) {
}