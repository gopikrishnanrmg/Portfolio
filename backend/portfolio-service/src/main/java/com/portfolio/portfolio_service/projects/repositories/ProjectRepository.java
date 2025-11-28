package com.portfolio.portfolio_service.projects.repositories;

import com.portfolio.portfolio_service.projects.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    boolean existsByTitle(String title);
}
