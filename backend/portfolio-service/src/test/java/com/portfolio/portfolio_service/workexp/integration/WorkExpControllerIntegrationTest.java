package com.portfolio.portfolio_service.workexp.integration;

import com.portfolio.portfolio_service.workexp.models.WorkExp;
import com.portfolio.portfolio_service.workexp.repositories.WorkExpRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class WorkExpControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WorkExpRepository workExpRepository;

    @BeforeEach
    void cleanDb() {
        workExpRepository.deleteAll();
    }

    @Test
    void createWorkExp_shouldPersistAndReturnResponse() throws Exception {
        String requestJson = """
            {
              "role": "Developer",
              "company": "CompanyX",
              "note": null,
              "startDate": "2020-01-01",
              "endDate": null,
              "points": ["Did stuff"]
            }
        """;

        mockMvc.perform(post("/api/v1/workexps")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header("X-API-KEY", "test-key"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.role").value("Developer"))
                .andExpect(jsonPath("$.company").value("CompanyX"))
                .andExpect(jsonPath("$.points[0]").value("Did stuff"));

        assertEquals(1, workExpRepository.findAll().size());
    }

    @Test
    void getAllWorkExps_shouldReturnSortedAndFiltered() throws Exception {
        WorkExp older = WorkExp.builder()
                .role("Older")
                .company("CompanyA")
                .startDate(LocalDate.of(2019,1,1))
                .points(List.of("Point"))
                .build();

        WorkExp newer = WorkExp.builder()
                .role("Newer")
                .company("CompanyB")
                .startDate(LocalDate.of(2021,1,1))
                .points(List.of("Point"))
                .build();

        workExpRepository.saveAll(List.of(older, newer));

        mockMvc.perform(get("/api/v1/workexps"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].role").value("Older"))
                .andExpect(jsonPath("$[1].role").value("Newer"));
    }

    @Test
    void patchWorkExp_shouldPartiallyUpdateFields() throws Exception {
        WorkExp workExp = workExpRepository.save(
                WorkExp.builder()
                        .role("Old Role")
                        .company("Old Company")
                        .note("Old note")
                        .startDate(LocalDate.of(2020, 1, 1))
                        .endDate(null)
                        .points(List.of("Old point"))
                        .isDeleted(false)
                        .build()
        );

        String patchJson = """
        {
          "role": "Updated Role"
        }
    """;

        mockMvc.perform(patch("/api/v1/workexps/" + workExp.getWorkExpId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson)
                        .header("X-API-KEY", "test-key"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("Updated Role"))
                .andExpect(jsonPath("$.company").value("Old Company"));

        WorkExp updated = workExpRepository.findById(workExp.getWorkExpId()).orElseThrow();
        assertEquals("Updated Role", updated.getRole());
        assertEquals("Old Company", updated.getCompany());
        assertEquals("Old note", updated.getNote());
    }

    @Test
    void replaceWorkExp_shouldFullyReplaceEntity() throws Exception {
        WorkExp workExp = workExpRepository.save(
                WorkExp.builder()
                        .role("Old Role")
                        .company("Old Company")
                        .note("Old note")
                        .startDate(LocalDate.of(2020, 1, 1))
                        .endDate(null)
                        .points(List.of("Old point"))
                        .isDeleted(false)
                        .build()
        );

        String replaceJson = """
        {
          "role": "New Role",
          "company": "New Company",
          "note": "New note",
          "startDate": "2023-01-01",
          "endDate": "2024-01-01",
          "points": ["New point"]
        }
    """;

        mockMvc.perform(put("/api/v1/workexps/" + workExp.getWorkExpId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(replaceJson)
                        .header("X-API-KEY", "test-key"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("New Role"))
                .andExpect(jsonPath("$.company").value("New Company"))
                .andExpect(jsonPath("$.note").value("New note"));

        WorkExp updated = workExpRepository.findById(workExp.getWorkExpId()).orElseThrow();
        assertEquals("New Role", updated.getRole());
        assertEquals("New Company", updated.getCompany());
        assertEquals("New note", updated.getNote());
        assertEquals(LocalDate.of(2023, 1, 1), updated.getStartDate());
        assertEquals(LocalDate.of(2024, 1, 1), updated.getEndDate());
        assertEquals(List.of("New point"), updated.getPoints());
    }

    @Test
    void deleteWorkExp_shouldMarkDeleted() throws Exception {
        WorkExp workExp = workExpRepository.save(
                WorkExp.builder()
                        .role("Dev")
                        .company("CompanyX")
                        .startDate(LocalDate.of(2020,1,1))
                        .points(List.of("Point"))
                        .build()
        );

        mockMvc.perform(delete("/api/v1/workexps/" + workExp.getWorkExpId())
                        .header("X-API-KEY", "test-key"))
                .andExpect(status().isNoContent());

        WorkExp deleted = workExpRepository.findById(workExp.getWorkExpId()).orElseThrow();
        assertTrue(deleted.getIsDeleted());
    }

    @Test
    void deleteWorkExpHard_shouldRemoveEntity() throws Exception {
        WorkExp workExp = workExpRepository.save(
                WorkExp.builder()
                        .role("Dev")
                        .company("CompanyX")
                        .startDate(LocalDate.of(2020,1,1))
                        .points(List.of("Point"))
                        .build()
        );

        mockMvc.perform(delete("/api/v1/workexps/hard/" + workExp.getWorkExpId())
                        .header("X-API-KEY", "test-key"))
                .andExpect(status().isNoContent());

        assertFalse(workExpRepository.findById(workExp.getWorkExpId()).isPresent());
    }
}
