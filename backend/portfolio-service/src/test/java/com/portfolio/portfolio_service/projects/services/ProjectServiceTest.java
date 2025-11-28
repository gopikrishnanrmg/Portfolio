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
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock private ProjectRepository projectRepository;
    @Mock private ProjectMapper projectMapper;

    @InjectMocks private ProjectService projectService;

    private final UUID projectId = UUID.randomUUID();

    @Test
    void createProject_shouldSaveAndReturnResponse() {
        CreateProjectRequest request = new CreateProjectRequest("Title", "Desc", List.of("Java"), "banner", "link");
        Project project = new Project(projectId, "Title", "Desc", List.of("Java"), "banner", "link", false);
        ProjectResponse expectedResponse = new ProjectResponse(projectId, "Title", "Desc", List.of("Java"), "banner", "link");

        when(projectRepository.existsByTitle("Title")).thenReturn(false);
        when(projectMapper.projectRequestToProject(request)).thenReturn(project);
        when(projectRepository.save(project)).thenReturn(project);
        when(projectMapper.projectToProjectResponse(project)).thenReturn(expectedResponse);

        ProjectResponse actualResponse = projectService.createProject(request);

        assertEquals(expectedResponse, actualResponse);
        verify(projectRepository).existsByTitle("Title");
        verify(projectMapper).projectRequestToProject(request);
        verify(projectRepository).save(project);
        verify(projectMapper).projectToProjectResponse(project);
    }

    @Test
    void createProject_shouldThrowDuplicateProjectException() {
        CreateProjectRequest request = new CreateProjectRequest("Title", "Desc", List.of("Java"), "banner", "link");
        when(projectRepository.existsByTitle("Title")).thenReturn(true);

        assertThrows(DuplicateProjectException.class, () -> projectService.createProject(request));

        verify(projectRepository).existsByTitle("Title");
        verifyNoMoreInteractions(projectRepository, projectMapper);
    }

    @Test
    void getAllProjects_shouldFilterDeleted() {
        Project active = new Project(projectId, "Active", "Desc", List.of("Java"), "banner", "link", false);
        Project deleted = new Project(UUID.randomUUID(), "Deleted", "Desc", List.of("Java"), "banner", "link", true);

        ProjectResponse activeResponse = new ProjectResponse(projectId, "Active", "Desc", List.of("Java"), "banner", "link");

        when(projectRepository.findAll()).thenReturn(List.of(active, deleted));
        when(projectMapper.projectToProjectResponse(active)).thenReturn(activeResponse);

        List<ProjectResponse> result = projectService.getAllProjects();

        assertEquals(1, result.size());
        assertEquals("Active", result.get(0).title());

        verify(projectRepository).findAll();
        verify(projectMapper).projectToProjectResponse(active);
        verify(projectMapper, never()).projectToProjectResponse(deleted);
    }

    @Test
    void updateProject_shouldReturnUpdatedResponse() {
        UpdateProjectRequest request = new UpdateProjectRequest("New Title", "New Desc", List.of("Spring"), "new-banner", Optional.of("new-link"));
        Project project = new Project(projectId, "Old Title", "Old Desc", List.of("Java"), "banner", "link", false);
        ProjectResponse response = new ProjectResponse(projectId, "New Title", "New Desc", List.of("Spring"), "new-banner", "new-link");

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(projectRepository.save(project)).thenReturn(project);
        when(projectMapper.projectToProjectResponse(project)).thenReturn(response);

        ProjectResponse result = projectService.updateProject(projectId, request);

        assertEquals("New Title", result.title());
        assertEquals("New Desc", result.description());

        verify(projectRepository).findById(projectId);
        verify(projectRepository).save(project);
        verify(projectMapper).projectToProjectResponse(project);
    }

    @Test
    void updateProject_shouldThrowWorkExpNotFoundException() {
        UpdateProjectRequest request = new UpdateProjectRequest("New Title", null, null, null, Optional.empty());
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThrows(WorkExpNotFoundException.class, () -> projectService.updateProject(projectId, request));

        verify(projectRepository).findById(projectId);
        verifyNoMoreInteractions(projectRepository, projectMapper);
    }

    @Test
    void updateProject_shouldThrowInvalidProjectUpdateException() {
        UpdateProjectRequest request = new UpdateProjectRequest("", null, null, null, Optional.empty());
        Project project = new Project(projectId, "Old Title", "Old Desc", List.of("Java"), "banner", "link", false);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        assertThrows(InvalidProjectUpdateException.class, () -> projectService.updateProject(projectId, request));

        verify(projectRepository).findById(projectId);
        verifyNoMoreInteractions(projectRepository, projectMapper);
    }

    @Test
    void deleteProject_shouldMarkDeleted() {
        Project project = new Project(projectId, "Title", "Desc", List.of("Java"), "banner", "link", false);
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        projectService.deleteProject(projectId);

        assertTrue(project.getIsDeleted());
        verify(projectRepository).findById(projectId);
        verify(projectRepository).save(project);
    }

    @Test
    void deleteProject_shouldThrowProjectNotFoundException() {
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> projectService.deleteProject(projectId));

        verify(projectRepository).findById(projectId);
        verifyNoMoreInteractions(projectRepository, projectMapper);
    }

    @Test
    void deleteProjectHard_shouldDelete() {
        Project project = new Project(projectId, "Title", "Desc", List.of("Java"), "banner", "link", false);
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        projectService.deleteProjectHard(projectId);

        verify(projectRepository).findById(projectId);
        verify(projectRepository).delete(project);
    }

    @Test
    void deleteProjectHard_shouldThrowProjectNotFoundException() {
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> projectService.deleteProjectHard(projectId));

        verify(projectRepository).findById(projectId);
        verifyNoMoreInteractions(projectRepository, projectMapper);
    }
}
