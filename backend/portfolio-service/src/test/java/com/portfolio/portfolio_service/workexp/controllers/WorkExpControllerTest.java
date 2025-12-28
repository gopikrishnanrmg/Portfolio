package com.portfolio.portfolio_service.workexp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.portfolio_service.workexp.dtos.CreateWorkExpRequest;
import com.portfolio.portfolio_service.workexp.dtos.ReplaceWorkExpRequest;
import com.portfolio.portfolio_service.workexp.dtos.WorkExpResponse;
import com.portfolio.portfolio_service.workexp.services.WorkExpService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WorkExpController.class)
@AutoConfigureMockMvc(addFilters = false)
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
                null,
                LocalDate.of(2020, 1, 1),
                null,
                List.of("Did stuff")
        );

        WorkExpResponse response = new WorkExpResponse(
                workExpId,
                "Developer",
                "CompanyX",
                null,
                LocalDate.of(2020, 1, 1),
                null,
                List.of("Did stuff")
        );

        when(workExpService.createWorkExp(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/workexps")
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
                "note",
                LocalDate.of(2020, 1, 1),
                null,
                List.of("Did stuff")
        );

        mockMvc.perform(post("/api/v1/workexps")
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
                "note",
                LocalDate.of(2020, 1, 1),
                null,
                List.of("Did stuff")
        );

        when(workExpService.getAllWorkExps()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/workexps"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].role").value("Developer"))
                .andExpect(jsonPath("$[0].company").value("CompanyX"))
                .andExpect(jsonPath("$[0].note").value("note"));
    }

    @Test
    void updateWorkExp_shouldReturnUpdated() throws Exception {
        String patchJson = """
        {
          "role": "Engineer",
          "company": "CompanyY",
          "note": null,
          "startDate": "2022-01-01",
          "endDate": null,
          "points": ["Updated"]
        }
        """;

        WorkExpResponse response = new WorkExpResponse(
                workExpId,
                "Engineer",
                "CompanyY",
                null,
                LocalDate.of(2022, 1, 1),
                null,
                List.of("Updated")
        );

        when(workExpService.updateWorkExp(eq(workExpId), any())).thenReturn(response);

        mockMvc.perform(patch("/api/v1/workexps/" + workExpId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workExpId").value(workExpId.toString()))
                .andExpect(jsonPath("$.role").value("Engineer"))
                .andExpect(jsonPath("$.company").value("CompanyY"))
                .andExpect(jsonPath("$.note").doesNotExist());

        verify(workExpService).updateWorkExp(eq(workExpId), any());
    }

    @Test
    void replaceWorkExp_shouldReturnUpdated() throws Exception {
        ReplaceWorkExpRequest request = new ReplaceWorkExpRequest(
                "New Role",
                "New Company",
                "New note",
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2024, 1, 1),
                List.of("New point")
        );

        WorkExpResponse response = new WorkExpResponse(
                workExpId,
                "New Role",
                "New Company",
                "New note",
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2024, 1, 1),
                List.of("New point")
        );

        when(workExpService.replaceWorkExp(eq(workExpId), any())).thenReturn(response);

        mockMvc.perform(put("/api/v1/workexps/" + workExpId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workExpId").value(workExpId.toString()))
                .andExpect(jsonPath("$.role").value("New Role"))
                .andExpect(jsonPath("$.company").value("New Company"))
                .andExpect(jsonPath("$.note").value("New note"));

        verify(workExpService).replaceWorkExp(eq(workExpId), any());
    }

    @Test
    void replaceWorkExp_shouldAllowNullOptionalFields() throws Exception {
        ReplaceWorkExpRequest request = new ReplaceWorkExpRequest(
                "New Role",
                "New Company",
                null,
                LocalDate.of(2023, 1, 1),
                null,
                List.of("Point")
        );

        WorkExpResponse response = new WorkExpResponse(
                workExpId,
                "New Role",
                "New Company",
                null,
                LocalDate.of(2023, 1, 1),
                null,
                List.of("Point")
        );

        when(workExpService.replaceWorkExp(eq(workExpId), any())).thenReturn(response);

        mockMvc.perform(put("/api/v1/workexps/" + workExpId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.note").doesNotExist())
                .andExpect(jsonPath("$.endDate").doesNotExist());

        verify(workExpService).replaceWorkExp(eq(workExpId), any());
    }

    @Test
    void replaceWorkExp_shouldReturnBadRequestForInvalidDTO() throws Exception {
        ReplaceWorkExpRequest request = new ReplaceWorkExpRequest(
                "",
                "Company",
                "note",
                LocalDate.of(2023, 1, 1),
                null,
                List.of("Point")
        );

        mockMvc.perform(put("/api/v1/workexps/" + workExpId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Error"))
                .andExpect(jsonPath("$.message").value(containsString("must not be blank")));
    }

    @Test
    void deleteWorkExp_shouldReturnNoContent() throws Exception {
        doNothing().when(workExpService).deleteWorkExp(workExpId);

        mockMvc.perform(delete("/api/v1/workexps/" + workExpId))
                .andExpect(status().isNoContent());

        verify(workExpService).deleteWorkExp(workExpId);
    }

    @Test
    void deleteWorkExpHard_shouldReturnNoContent() throws Exception {
        doNothing().when(workExpService).deleteWorkExpHard(workExpId);

        mockMvc.perform(delete("/api/v1/workexps/hard/" + workExpId))
                .andExpect(status().isNoContent());

        verify(workExpService).deleteWorkExpHard(workExpId);
    }
}
