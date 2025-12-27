package com.portfolio.portfolio_service.testimonials.dtos;

import com.fasterxml.jackson.databind.JsonNode;

public record UpdateTestimonialRequest(JsonNode name, JsonNode role, JsonNode text, JsonNode initials, JsonNode accent) {
}
