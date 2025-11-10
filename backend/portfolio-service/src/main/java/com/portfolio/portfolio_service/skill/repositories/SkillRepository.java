package com.portfolio.portfolio_service.skill.repositories;

import com.portfolio.portfolio_service.skill.models.Category;
import com.portfolio.portfolio_service.skill.models.Skill;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SkillRepository extends JpaRepository<Skill, UUID> {
    List<Skill> findByCategory(Category category);
    boolean existsByName(String name);
}
