package com.portfolio.portfolio_service.projects.dtos;

import java.util.List;
import java.util.Optional;

public record UpdateProjectRequest(String title, String description, List<String> tech, String banner, Optional<String> link) {
}
