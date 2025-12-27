package com.portfolio.portfolio_service.projects.dtos;

import com.fasterxml.jackson.databind.JsonNode;

public record UpdateProjectRequest(JsonNode title, JsonNode description, JsonNode tech, JsonNode banner, JsonNode link) {
}
