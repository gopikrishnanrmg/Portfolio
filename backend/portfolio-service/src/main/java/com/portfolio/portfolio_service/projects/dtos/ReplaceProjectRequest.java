package com.portfolio.portfolio_service.projects.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ReplaceProjectRequest(
        @NotBlank String title,
        @NotBlank String description,
        @NotEmpty List<String> tech,
        @NotBlank String banner,
        String link
) {}

