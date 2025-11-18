package com.portfolio.portfolio_service.skill.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.portfolio_service.skill.dtos.CreateSkillRequest;
import com.portfolio.portfolio_service.skill.dtos.SkillResponse;
import com.portfolio.portfolio_service.skill.dtos.UpdateSkillRequest;
import com.portfolio.portfolio_service.skill.models.Category;
import com.portfolio.portfolio_service.skill.services.SkillService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@ActiveProfiles("test")
@WebMvcTest(SkillController.class)
@RequiredArgsConstructor
class SkillControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SkillService skillService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final UUID skillId = UUID.randomUUID();

    @Test
    void createSkill_shouldReturnCreated() throws Exception {
        CreateSkillRequest request = new CreateSkillRequest(Category.DEVELOPMENT, "React");
        MockMultipartFile file = new MockMultipartFile("file", "icon.png", "image/png", "dummy".getBytes());
        MockMultipartFile data = new MockMultipartFile("data", "", "application/json",
                objectMapper.writeValueAsBytes(request));

        SkillResponse response = new SkillResponse(skillId, Category.DEVELOPMENT, "React", "url");

        when(skillService.createSkill(any(), any())).thenReturn(response);

        mockMvc.perform(multipart("/api/v1/skills")
                        .file(data)
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.skillId").value(skillId.toString()));
    }

    @Test
    void createSkill_shouldThrowDTOValidationError() throws Exception {
        CreateSkillRequest request = new CreateSkillRequest(Category.DEVELOPMENT, null);
        MockMultipartFile file = new MockMultipartFile("file", "icon.png", "image/png", "dummy".getBytes());
        MockMultipartFile data = new MockMultipartFile("data", "", "application/json",
                objectMapper.writeValueAsBytes(request));

        SkillResponse response = new SkillResponse(skillId, Category.DEVELOPMENT, "React", "url");

        when(skillService.createSkill(any(), any())).thenReturn(response);

        mockMvc.perform(multipart("/api/v1/skills")
                        .file(data)
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("must not be blank")))
                .andExpect(jsonPath("$.error").value("Validation Error"));
    }

    @Test
    void createSkill_shouldThrowFileValidationError() throws Exception {
        CreateSkillRequest request = new CreateSkillRequest(Category.DEVELOPMENT, "React");
        MockMultipartFile file = new MockMultipartFile("file", "icon.png", "image/png", "".getBytes());
        MockMultipartFile data = new MockMultipartFile("data", "", "application/json",
                objectMapper.writeValueAsBytes(request));

        SkillResponse response = new SkillResponse(skillId, Category.DEVELOPMENT, "React", "url");

        when(skillService.createSkill(any(), any())).thenReturn(response);

        mockMvc.perform(multipart("/api/v1/skills")
                        .file(data)
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("must be present and not empty")))
                .andExpect(jsonPath("$.error").value("Validation Error"));
    }

    @Test
    void getAllSkills_shouldReturnList() throws Exception {
        List<SkillResponse> skills = List.of(new SkillResponse(skillId, Category.DEVELOPMENT, "React", "url"));
        when(skillService.getAllSkills()).thenReturn(skills);

        mockMvc.perform(get("/api/v1/skills"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("React"));
    }

    @Test
    void getSkillsByCategory_shouldReturnList() throws Exception {
        List<SkillResponse> skills = List.of(new SkillResponse(skillId, Category.DEVELOPMENT,"React", "url"));
        when(skillService.getSkillsByCategory(Category.DEVELOPMENT)).thenReturn(skills);

        mockMvc.perform(get("/api/v1/skills/development"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("DEVELOPMENT"));
    }

    @Test
    void updateSkill_shouldReturnUpdatedSkill() throws Exception {
        UpdateSkillRequest request = new UpdateSkillRequest(Category.DEVELOPMENT, "Spring Boot");
        SkillResponse response = new SkillResponse(skillId, Category.DEVELOPMENT, "Spring Boot", "url");

        when(skillService.updateSkill(eq(skillId), any())).thenReturn(response);

        mockMvc.perform(put("/api/v1/skills/" + skillId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.skillId").value(skillId.toString()))
                .andExpect(jsonPath("$.name").value("Spring Boot"))
                .andExpect(jsonPath("$.category").value("DEVELOPMENT"))
                .andExpect(jsonPath("$.url").value("url"));

        verify(skillService).updateSkill(eq(skillId), any());
    }

    @Test
    void uploadSkillFile_shouldReturnUpdatedSkill() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "icon.png", "image/png", "dummy".getBytes());
        SkillResponse response = new SkillResponse(skillId, Category.DEVELOPMENT, "React", "url");

        when(skillService.uploadSkillFile(eq(skillId), any())).thenReturn(response);

        mockMvc.perform(multipart("/api/v1/skills/" + skillId + "/file")
                        .file(file)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value("url"));
    }

    @Test
    void deleteSkill_shouldReturnNoContent() throws Exception {
        doNothing().when(skillService).deleteSkill(skillId);

        mockMvc.perform(delete("/api/v1/skills/" + skillId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteSkillHard_shouldReturnNoContent() throws Exception {
        doNothing().when(skillService).deleteSkillHard(skillId);

        mockMvc.perform(delete("/api/v1/skills/hard/" + skillId))
                .andExpect(status().isNoContent());
    }

}