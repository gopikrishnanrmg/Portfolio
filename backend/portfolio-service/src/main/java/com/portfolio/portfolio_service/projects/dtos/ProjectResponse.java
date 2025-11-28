package com.portfolio.portfolio_service.projects.dtos;

import java.util.List;
import java.util.UUID;

public record ProjectResponse(UUID projectId, String title, String description, List<String> tech, String banner, String link) {
}
