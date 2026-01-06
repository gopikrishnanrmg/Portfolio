package com.portfolio.portfolio_service.skill.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.portfolio_service.skill.dtos.CreateSkillRequest;
import com.portfolio.portfolio_service.skill.dtos.UpdateSkillRequest;
import com.portfolio.portfolio_service.skill.models.Category;
import com.portfolio.portfolio_service.skill.models.Skill;
import com.portfolio.portfolio_service.skill.repositories.SkillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SkillControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SkillRepository skillRepository;

    @BeforeEach
    void cleanDb() {
        skillRepository.deleteAll();
    }

    @Test
    void createSkill_shouldPersistAndReturnResponse() throws Exception {
        CreateSkillRequest request = new CreateSkillRequest(Category.DEVELOPMENT, "React");

        MockMultipartFile file = new MockMultipartFile("file", "icon.png", "image/png", "dummy".getBytes());
        MockMultipartFile data = new MockMultipartFile("data", "", "application/json",
                new ObjectMapper().writeValueAsBytes(request));

        mockMvc.perform(multipart("/api/v1/skills")
                        .file(file)
                        .file(data)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("X-API-KEY", "test-key"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("React"));

        assertTrue(skillRepository.existsByNameAndIsDeletedFalse("React"));
    }

    @Test
    void getAllSkills_shouldReturnList() throws Exception {
        skillRepository.save(
                Skill.builder()
                        .category(Category.DEVELOPMENT)
                        .name("Java")
                        .storageKey("fakeKey")
                        .isDeleted(false)
                        .build()
        );

        mockMvc.perform(get("/api/v1/skills"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Java"));
    }

    @Test
    void getSkillsByCategory_shouldReturnFilteredList() throws Exception {
        skillRepository.save(
                Skill.builder()
                        .category(Category.DEVELOPMENT)
                        .name("Java")
                        .storageKey("fakeKey")
                        .isDeleted(false)
                        .build()
        );
        skillRepository.save(
                Skill.builder()
                        .category(Category.ARCHITECTURE)
                        .name("UML")
                        .storageKey("fakeKey")
                        .isDeleted(false)
                        .build()
        );

        mockMvc.perform(get("/api/v1/skills/DEVELOPMENT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Java"));
    }

    @Test
    void updateSkill_shouldUpdateFields() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Skill skill = skillRepository.save(
                Skill.builder()
                        .category(Category.DEVELOPMENT)
                        .name("Java")
                        .storageKey("fakeKey")
                        .isDeleted(false)
                        .build()
        );

        UpdateSkillRequest update = new UpdateSkillRequest(
                mapper.readTree("\"DEVELOPMENT\""),
                mapper.readTree("\"Kotlin\"")
        );

        mockMvc.perform(patch("/api/v1/skills/" + skill.getSkillId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(update))
                        .header("X-API-KEY", "test-key"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Kotlin"));
    }

    @Test
    void uploadSkillFile_shouldReplaceFile() throws Exception {
        Skill skill = skillRepository.save(
                Skill.builder()
                        .category(Category.DEVELOPMENT)
                        .name("Java")
                        .storageKey("oldKey")
                        .isDeleted(false)
                        .build()
        );

        MockMultipartFile file = new MockMultipartFile("file", "icon.png", "image/png", "dummy".getBytes());

        mockMvc.perform(multipart("/api/v1/skills/" + skill.getSkillId() + "/file")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("X-API-KEY", "test-key")
                        .with(request -> { request.setMethod("PUT"); return request; }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Java"));
    }

    @Test
    void deleteSkill_shouldMarkDeleted() throws Exception {
        Skill skill = skillRepository.save(
                Skill.builder()
                        .category(Category.DEVELOPMENT)
                        .name("Java")
                        .storageKey("fakeKey")
                        .isDeleted(false)
                        .build()
        );

        mockMvc.perform(delete("/api/v1/skills/" + skill.getSkillId())
                        .header("X-API-KEY", "test-key"))
                .andExpect(status().isNoContent());

        assertTrue(skillRepository.findById(skill.getSkillId()).get().getIsDeleted());
    }

    @Test
    void deleteSkillHard_shouldRemoveEntity() throws Exception {
        Skill skill = skillRepository.save(
                Skill.builder()
                        .category(Category.DEVELOPMENT)
                        .name("Java")
                        .storageKey("fakeKey")
                        .isDeleted(false)
                        .build()
        );

        mockMvc.perform(delete("/api/v1/skills/hard/" + skill.getSkillId())
                        .header("X-API-KEY", "test-key"))
                .andExpect(status().isNoContent());

        assertFalse(skillRepository.findById(skill.getSkillId()).isPresent());
    }
}

