package com.portfolio.portfolio_service.projects.integration;

import com.portfolio.portfolio_service.projects.models.Project;
import com.portfolio.portfolio_service.projects.repositories.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProjectControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectRepository projectRepository;

    @BeforeEach
    void cleanDb() { projectRepository.deleteAll(); }

    @Test
    void createProject_shouldPersistAndReturnResponse() throws Exception {
        String requestJson = """
            {
              "title": "Portfolio App",
              "description": "A sample project",
              "tech": ["Java","Spring"],
              "banner": "from-pink-500 via-purple-500 to-cyan-400",
              "link": "http://example.com"
            }
        """;

        mockMvc.perform(post("/api/v1/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Portfolio App"))
                .andExpect(jsonPath("$.description").value("A sample project"))
                .andExpect(jsonPath("$.tech[0]").value("Java"));

        assertEquals(1, projectRepository.findAll().size());
    }

    @Test
    void getAllProjects_shouldReturnFiltered() throws Exception {
        Project active = Project.builder()
                .title("Active")
                .description("Desc")
                .tech(List.of("Java"))
                .banner("from-pink-500 via-purple-500 to-cyan-400")
                .link("http://active.com")
                .isDeleted(false)
                .build();

        Project deleted = Project.builder()
                .title("Deleted")
                .description("Desc")
                .tech(List.of("Java"))
                .banner("from-pink-500 via-purple-500 to-cyan-400")
                .link("http://deleted.com")
                .isDeleted(true)
                .build();

        projectRepository.saveAll(List.of(active, deleted));

        mockMvc.perform(get("/api/v1/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Active"));
    }

    @Test
    void updateProject_shouldUpdateFields() throws Exception {
        Project project = projectRepository.save(
                Project.builder()
                        .title("Old Title")
                        .description("Old Desc")
                        .tech(List.of("Java"))
                        .banner("from-pink-500 via-purple-500 to-cyan-400")
                        .link("http://old.com")
                        .isDeleted(false)
                        .build()
        );

        String updateJson = """
            {
              "title": "New Title",
              "description": "New Desc",
              "tech": ["Spring Boot"],
              "banner": "from-fuchsia-500 via-rose-400 to-amber-400",
              "link": "http://new.com"
            }
        """;

        mockMvc.perform(put("/api/v1/projects/" + project.getProjectId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Title"))
                .andExpect(jsonPath("$.description").value("New Desc"));

        Project updated = projectRepository.findById(project.getProjectId()).orElseThrow();
        assertEquals("New Title", updated.getTitle());
        assertEquals("New Desc", updated.getDescription());
    }

    @Test
    void deleteProject_shouldMarkDeleted() throws Exception {
        Project project = projectRepository.save(
                Project.builder()
                        .title("ToDelete")
                        .description("Desc")
                        .tech(List.of("Java"))
                        .banner("from-pink-500 via-purple-500 to-cyan-400")
                        .link("http://delete.com")
                        .isDeleted(false)
                        .build()
        );

        mockMvc.perform(delete("/api/v1/projects/" + project.getProjectId()))
                .andExpect(status().isNoContent());

        Project deleted = projectRepository.findById(project.getProjectId()).orElseThrow();
        assertTrue(deleted.getIsDeleted());
    }

    @Test
    void deleteProjectHard_shouldRemoveEntity() throws Exception {
        Project project = projectRepository.save(
                Project.builder()
                        .title("HardDelete")
                        .description("Desc")
                        .tech(List.of("Java"))
                        .banner("from-pink-500 via-purple-500 to-cyan-400")
                        .link("http://harddelete.com")
                        .isDeleted(false)
                        .build()
        );

        mockMvc.perform(delete("/api/v1/projects/hard/" + project.getProjectId()))
                .andExpect(status().isNoContent());

        assertFalse(projectRepository.findById(project.getProjectId()).isPresent());
    }
}
