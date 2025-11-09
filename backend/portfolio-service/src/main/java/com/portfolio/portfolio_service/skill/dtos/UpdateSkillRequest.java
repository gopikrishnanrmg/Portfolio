package com.portfolio.portfolio_service.skill.dtos;

import com.portfolio.portfolio_service.skill.models.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateSkillRequest(Category category, String name) {
}
