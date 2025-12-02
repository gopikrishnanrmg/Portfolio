package com.portfolio.portfolio_service.testimonials.dtos;

import jakarta.validation.constraints.NotBlank;

public record CreateTestimonialRequest(@NotBlank String name, @NotBlank String role, @NotBlank String text, @NotBlank String initials, @NotBlank String accent) {
}
