package com.portfolio.portfolio_service.skill.dtos;

import com.portfolio.portfolio_service.skill.models.Category;

public record SkillRequest(Category category, String name) {
}
