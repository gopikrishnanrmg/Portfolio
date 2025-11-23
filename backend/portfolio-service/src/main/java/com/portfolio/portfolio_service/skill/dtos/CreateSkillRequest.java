package com.portfolio.portfolio_service.skill.dtos;

import com.portfolio.portfolio_service.skill.models.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateSkillRequest(@NotNull Category category, @NotBlank String name) {
}
