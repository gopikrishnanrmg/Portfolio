package com.portfolio.portfolio_service.projects.services;

import com.portfolio.portfolio_service.projects.dtos.CreateProjectRequest;
import com.portfolio.portfolio_service.projects.dtos.ProjectResponse;
import com.portfolio.portfolio_service.projects.dtos.ReplaceProjectRequest;
import com.portfolio.portfolio_service.projects.dtos.UpdateProjectRequest;
import com.portfolio.portfolio_service.projects.exceptions.DuplicateProjectException;
import com.portfolio.portfolio_service.projects.exceptions.InvalidProjectUpdateException;
import com.portfolio.portfolio_service.projects.exceptions.ProjectNotFoundException;
import com.portfolio.portfolio_service.projects.mappers.ProjectMapper;
import com.portfolio.portfolio_service.projects.models.Project;
import com.portfolio.portfolio_service.projects.repositories.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public ProjectResponse createProject(CreateProjectRequest projectRequest) {
        if (projectRepository.existsByTitleAndIsDeletedFalse(projectRequest.title()))
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
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + projectId));

        if (projectRequest.title() != null) {
            if (projectRequest.title().isNull())
                throw new InvalidProjectUpdateException("Title cannot be null");
            String value = projectRequest.title().asText();
            if (value.isBlank())
                throw new InvalidProjectUpdateException("Title cannot be blank");
            project.setTitle(value);
        }

        if (projectRequest.description() != null) {
            if (projectRequest.description().isNull())
                throw new InvalidProjectUpdateException("Description cannot be null");
            String value = projectRequest.description().asText();
            if (value.isBlank())
                throw new InvalidProjectUpdateException("Description cannot be blank");
            project.setDescription(value);
        }

        if (projectRequest.tech() != null) {
            if (projectRequest.tech().isNull())
                throw new InvalidProjectUpdateException("Tech cannot be null");

            List<String> techList = new ArrayList<>();
            projectRequest.tech().forEach(node -> techList.add(node.asText()));

            if (techList.isEmpty())
                throw new InvalidProjectUpdateException("Tech cannot be empty");

            project.setTech(techList);
        }

        if (projectRequest.banner() != null) {
            if (projectRequest.banner().isNull())
                throw new InvalidProjectUpdateException("Banner cannot be null");
            String value = projectRequest.banner().asText();
            if (value.isBlank())
                throw new InvalidProjectUpdateException("Banner cannot be blank");
            project.setBanner(value);
        }

        if (projectRequest.link() != null) {
            if (projectRequest.link().isNull()) {
                project.setLink(null);
            } else {
                project.setLink(projectRequest.link().asText());
            }
        }

        Project updated = projectRepository.save(project);
        return projectMapper.projectToProjectResponse(updated);
    }

    public ProjectResponse replaceProject(UUID id, ReplaceProjectRequest req) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Not found"));

        project.setTitle(req.title());
        project.setDescription(req.description());
        project.setTech(req.tech());
        project.setBanner(req.banner());
        project.setLink(req.link());

        return projectMapper.projectToProjectResponse(projectRepository.save(project));
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
