package com.portfolio.portfolio_service.projects.services;

import com.portfolio.portfolio_service.projects.dtos.CreateProjectRequest;
import com.portfolio.portfolio_service.projects.dtos.ProjectResponse;
import com.portfolio.portfolio_service.projects.dtos.UpdateProjectRequest;
import com.portfolio.portfolio_service.projects.exceptions.DuplicateProjectException;
import com.portfolio.portfolio_service.projects.exceptions.InvalidProjectUpdateException;
import com.portfolio.portfolio_service.projects.exceptions.ProjectNotFoundException;
import com.portfolio.portfolio_service.projects.mappers.ProjectMapper;
import com.portfolio.portfolio_service.projects.models.Project;
import com.portfolio.portfolio_service.projects.repositories.ProjectRepository;
import com.portfolio.portfolio_service.workexp.exceptions.WorkExpNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public ProjectResponse createProject(@Valid CreateProjectRequest projectRequest) {
        if (projectRepository.existsByTitle(projectRequest.title()))
            throw new DuplicateProjectException("Project title already exists");

        Project project = projectMapper.projectRequestToProject(projectRequest);
        Project saved = projectRepository.save(project);
        return projectMapper.projectToProjectResponse(saved);
    }

    public List<ProjectResponse> getAllProjects() {
        return projectRepository.findAll()
                    .stream()
                    .filter(project -> !project.getIsDeleted())
                    .map(projectMapper::projectToProjectResponse)
                    .toList();
    }

    public ProjectResponse updateProject(UUID projectId, UpdateProjectRequest projectRequest) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new WorkExpNotFoundException("Project not found with id: " + projectId));

        if (projectRequest.title() != null) {
            if (!projectRequest.title().isBlank())
                project.setTitle(projectRequest.title());
            else
                throw new InvalidProjectUpdateException("Project cannot be blank");
        }

        if (projectRequest.description() != null) {
            if (!projectRequest.description().isBlank())
                project.setDescription(projectRequest.description());
            else
                throw new InvalidProjectUpdateException("Description cannot be blank");
        }

        if (projectRequest.tech() != null) {
            if (!projectRequest.tech().isEmpty())
                project.setTech(projectRequest.tech());
            else
                throw new InvalidProjectUpdateException("Tech cannot be empty");
        }

        if (projectRequest.banner() != null) {
            if (!projectRequest.banner().isBlank())
                project.setBanner(projectRequest.banner());
            else
                throw new InvalidProjectUpdateException("Banner cannot be blank");
        }

        if (projectRequest.description() != null) {
            if (!projectRequest.description().isBlank())
                project.setDescription(projectRequest.description());
            else
                throw new InvalidProjectUpdateException("Link cannot be blank");
        }

        Project updated = projectRepository.save(project);
        return projectMapper.projectToProjectResponse(updated);
    }

    public void deleteProject(UUID projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + projectId));

        project.setIsDeleted(true);
        projectRepository.save(project);
    }

    public void deleteProjectHard(UUID projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + projectId));

        projectRepository.delete(project);
    }
}
