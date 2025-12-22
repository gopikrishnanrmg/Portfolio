package com.portfolio.portfolio_service.skill.dtos;

import com.fasterxml.jackson.databind.JsonNode;

public record UpdateSkillRequest(JsonNode category, JsonNode name) {
}
