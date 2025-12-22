package com.portfolio.portfolio_service.projects.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.portfolio_service.projects.dtos.CreateProjectRequest;
import com.portfolio.portfolio_service.projects.dtos.ProjectResponse;
import com.portfolio.portfolio_service.projects.dtos.ReplaceProjectRequest;
import com.portfolio.portfolio_service.projects.dtos.UpdateProjectRequest;
import com.portfolio.portfolio_service.projects.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
@RequiredArgsConstructor
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    private final UUID projectId = UUID.randomUUID();

    @Test
    void createProject_shouldReturnCreated() throws Exception {
        CreateProjectRequest request = new CreateProjectRequest(
                "Portfolio App",
                "A sample project",
                List.of("Java", "Spring"),
                "from-pink-500 via-purple-500 to-cyan-400",
                "http://example.com"
        );

        ProjectResponse response = new ProjectResponse(
                projectId,
                "Portfolio App",
                "A sample project",
                List.of("Java", "Spring"),
                "from-pink-500 via-purple-500 to-cyan-400",
                "http://example.com"
        );

        when(projectService.createProject(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/projects/" + projectId))
                .andExpect(jsonPath("$.projectId").value(projectId.toString()))
                .andExpect(jsonPath("$.title").value("Portfolio App"))
                .andExpect(jsonPath("$.description").value("A sample project"));
    }

    @Test
    void createProject_shouldThrowDTOValidationError() throws Exception {
        CreateProjectRequest request = new CreateProjectRequest(
                "",
                "A sample project",
                List.of("Java"),
                "from-pink-500 via-purple-500 to-cyan-400",
                "http://example.com"
        );

        mockMvc.perform(post("/api/v1/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Error"))
                .andExpect(jsonPath("$.message").value(containsString("must not be blank")));
    }

    @Test
    void getAllProjects_shouldReturnList() throws Exception {
        ProjectResponse response = new ProjectResponse(
                projectId,
                "Portfolio App",
                "A sample project",
                List.of("Java", "Spring"),
                "from-pink-500 via-purple-500 to-cyan-400",
                "http://example.com"
        );

        when(projectService.getAllProjects()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].projectId").value(projectId.toString()))
                .andExpect(jsonPath("$[0].title").value("Portfolio App"))
                .andExpect(jsonPath("$[0].description").value("A sample project"));
    }

    @Test
    void updateProject_shouldReturnUpdated() throws Exception {
        String patchJson = """
        {
          "title": "Updated Title",
          "description": "Updated Description",
          "tech": ["Spring Boot"],
          "banner": "from-fuchsia-500 via-rose-400 to-amber-400",
          "link": "http://updated.com"
        }
    """;

        ProjectResponse response = new ProjectResponse(
                projectId,
                "Updated Title",
                "Updated Description",
                List.of("Spring Boot"),
                "from-fuchsia-500 via-rose-400 to-amber-400",
                "http://updated.com"
        );

        when(projectService.updateProject(eq(projectId), any())).thenReturn(response);

        mockMvc.perform(patch("/api/v1/projects/" + projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectId").value(projectId.toString()))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.description").value("Updated Description"));

        verify(projectService).updateProject(eq(projectId), any());
    }

    @Test
    void replaceProject_shouldReturnUpdated() throws Exception {
        ReplaceProjectRequest request = new ReplaceProjectRequest(
                "New Title",
                "New Description",
                List.of("Go"),
                "new-banner",
                "http://new-link.com"
        );

        ProjectResponse response = new ProjectResponse(
                projectId,
                "New Title",
                "New Description",
                List.of("Go"),
                "new-banner",
                "http://new-link.com"
        );

        when(projectService.replaceProject(eq(projectId), any())).thenReturn(response);

        mockMvc.perform(put("/api/v1/projects/" + projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectId").value(projectId.toString()))
                .andExpect(jsonPath("$.title").value("New Title"))
                .andExpect(jsonPath("$.description").value("New Description"));

        verify(projectService).replaceProject(eq(projectId), any());
    }

    @Test
    void replaceProject_shouldAllowNullOptionalFields() throws Exception {
        ReplaceProjectRequest request = new ReplaceProjectRequest(
                "New Title",
                "New Description",
                List.of("Go"),
                "new-banner",
                null
        );

        ProjectResponse response = new ProjectResponse(
                projectId,
                "New Title",
                "New Description",
                List.of("Go"),
                "new-banner",
                null
        );

        when(projectService.replaceProject(eq(projectId), any())).thenReturn(response);

        mockMvc.perform(put("/api/v1/projects/" + projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.link").doesNotExist());

        verify(projectService).replaceProject(eq(projectId), any());
    }

    @Test
    void replaceProject_shouldReturnBadRequestForInvalidDTO() throws Exception {
        ReplaceProjectRequest request = new ReplaceProjectRequest(
                "",
                "New Description",
                List.of("Go"),
                "new-banner",
                "http://new-link.com"
        );

        mockMvc.perform(put("/api/v1/projects/" + projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Error"))
                .andExpect(jsonPath("$.message").value(containsString("must not be blank")));
    }


    @Test
    void deleteProject_shouldReturnNoContent() throws Exception {
        doNothing().when(projectService).deleteProject(projectId);

        mockMvc.perform(delete("/api/v1/projects/" + projectId))
                .andExpect(status().isNoContent());

        verify(projectService).deleteProject(projectId);
    }

    @Test
    void deleteProjectHard_shouldReturnNoContent() throws Exception {
        doNothing().when(projectService).deleteProjectHard(projectId);

        mockMvc.perform(delete("/api/v1/projects/hard/" + projectId))
                .andExpect(status().isNoContent());

        verify(projectService).deleteProjectHard(projectId);
    }
}
