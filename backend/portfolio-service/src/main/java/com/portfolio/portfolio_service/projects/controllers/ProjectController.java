package com.portfolio.portfolio_service.projects.controllers;

import com.portfolio.portfolio_service.projects.dtos.CreateProjectRequest;
import com.portfolio.portfolio_service.projects.dtos.ProjectResponse;
import com.portfolio.portfolio_service.projects.dtos.UpdateProjectRequest;
import com.portfolio.portfolio_service.projects.services.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping()
    public ResponseEntity<ProjectResponse> createProject(
            @Valid @RequestBody CreateProjectRequest projectRequest) {
        ProjectResponse created = projectService.createProject(projectRequest);
        URI location = URI.create("/api/v1/projects/" + created.projectId());
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping()
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {
        return ResponseEntity.ok().body(projectService.getAllProjects());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(
            @RequestBody UpdateProjectRequest projectRequest,
            @PathVariable UUID id) {
        return ResponseEntity.ok(projectService.updateProject(id, projectRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/hard/{id}")
    public ResponseEntity<Void> deleteByIdHard(@PathVariable UUID id) {
        projectService.deleteProjectHard(id);
        return ResponseEntity.noContent().build();
    }
}
