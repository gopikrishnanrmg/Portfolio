package com.portfolio.portfolio_service.workexp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.portfolio_service.workexp.dtos.CreateWorkExpRequest;
import com.portfolio.portfolio_service.workexp.dtos.UpdateWorkExpRequest;
import com.portfolio.portfolio_service.workexp.dtos.WorkExpResponse;
import com.portfolio.portfolio_service.workexp.services.WorkExpService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(WorkExpController.class)
@RequiredArgsConstructor
class WorkExpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WorkExpService workExpService;

    @Autowired
    private ObjectMapper objectMapper;

    private final UUID workExpId = UUID.randomUUID();

    @Test
    void createWorkExp_shouldReturnCreated() throws Exception {
        CreateWorkExpRequest request = new CreateWorkExpRequest(
                "Developer",
                "CompanyX",
                LocalDate.of(2020, 1, 1),
                null,
                List.of("Did stuff")
        );

        WorkExpResponse response = new WorkExpResponse(
                workExpId,
                "Developer",
                "CompanyX",
                LocalDate.of(2020, 1, 1),
                null,
                List.of("Did stuff")
        );

        when(workExpService.createWorkExp(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/workexp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.workExpId").value(workExpId.toString()))
                .andExpect(jsonPath("$.role").value("Developer"))
                .andExpect(jsonPath("$.company").value("CompanyX"));
    }

    @Test
    void createWorkExp_shouldThrowDTOValidationError() throws Exception {
        CreateWorkExpRequest request = new CreateWorkExpRequest(
                "",
                "CompanyX",
                LocalDate.of(2020, 1, 1),
                null,
                List.of("Did stuff")
        );

        mockMvc.perform(post("/api/v1/workexp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Error"))
                .andExpect(jsonPath("$.message").value(containsString("must not be blank")));
    }

    @Test
    void getAllWorkExps_shouldReturnList() throws Exception {
        WorkExpResponse response = new WorkExpResponse(
                workExpId,
                "Developer",
                "CompanyX",
                LocalDate.of(2020, 1, 1),
                null,
                List.of("Did stuff")
        );

        when(workExpService.getAllWorkExps()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/workexp"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].role").value("Developer"))
                .andExpect(jsonPath("$[0].company").value("CompanyX"));
    }

    @Test
    void updateWorkExp_shouldReturnUpdated() throws Exception {
        UpdateWorkExpRequest request = new UpdateWorkExpRequest(
                "Engineer",
                "CompanyY",
                LocalDate.of(2022, 1, 1),
                Optional.empty(),
                List.of("Updated")
        );

        WorkExpResponse response = new WorkExpResponse(
                workExpId,
                "Engineer",
                "CompanyY",
                LocalDate.of(2022, 1, 1),
                null,
                List.of("Updated")
        );

        when(workExpService.updateWorkExp(eq(workExpId), any())).thenReturn(response);

        mockMvc.perform(put("/api/v1/workexp/" + workExpId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workExpId").value(workExpId.toString()))
                .andExpect(jsonPath("$.role").value("Engineer"))
                .andExpect(jsonPath("$.company").value("CompanyY"));

        verify(workExpService).updateWorkExp(eq(workExpId), any());
    }

    @Test
    void deleteWorkExp_shouldReturnNoContent() throws Exception {
        doNothing().when(workExpService).deleteWorkExp(workExpId);

        mockMvc.perform(delete("/api/v1/workexp/" + workExpId))
                .andExpect(status().isNoContent());

        verify(workExpService).deleteWorkExp(workExpId);
    }

    @Test
    void deleteWorkExpHard_shouldReturnNoContent() throws Exception {
        doNothing().when(workExpService).deleteWorkExpHard(workExpId);

        mockMvc.perform(delete("/api/v1/workexp/hard/" + workExpId))
                .andExpect(status().isNoContent());

        verify(workExpService).deleteWorkExpHard(workExpId);
    }
}
