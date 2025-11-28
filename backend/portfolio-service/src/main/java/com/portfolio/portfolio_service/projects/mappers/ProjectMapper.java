package com.portfolio.portfolio_service.projects.mappers;

import com.portfolio.portfolio_service.projects.dtos.CreateProjectRequest;
import com.portfolio.portfolio_service.projects.dtos.ProjectResponse;
import com.portfolio.portfolio_service.projects.models.Project;
import com.portfolio.portfolio_service.workexp.dtos.WorkExpResponse;
import com.portfolio.portfolio_service.workexp.models.WorkExp;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {
    public ProjectResponse projectToProjectResponse(Project project) {
        return new ProjectResponse(project.getProjectId(), project.getTitle(), project.getDescription(), project.getTech(), project.getBanner(), project.getLink());
    }

    public Project projectRequestToProject(CreateProjectRequest projectRequest) {
        return Project
                .builder()
                .title(projectRequest.title())
                .description(projectRequest.description())
                .tech(projectRequest.tech())
                .banner(projectRequest.banner())
                .link(projectRequest.link())
                .build();
    }
}
