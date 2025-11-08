package com.portfolio.portfolio_service.skill.dtos;

import com.portfolio.portfolio_service.skill.models.Category;

import java.util.UUID;

public record SkillResponse(UUID skillId, Category category, String name, String url) {
}
