package com.portfolio.portfolio_service.skill;

import java.util.UUID;

public record ResponseSkillDTO(UUID skillId, Category category, String name, byte[] resource) {
}
