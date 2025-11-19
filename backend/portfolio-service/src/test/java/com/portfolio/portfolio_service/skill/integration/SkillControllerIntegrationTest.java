package com.portfolio.portfolio_service.skill.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.portfolio.portfolio_service.blob_storage.StorageService;
import com.portfolio.portfolio_service.blob_storage.dtos.StorageResult;
import com.portfolio.portfolio_service.skill.dtos.CreateSkillRequest;
import com.portfolio.portfolio_service.skill.models.Category;
import com.portfolio.portfolio_service.skill.repositories.SkillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
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

    @MockitoBean
    private StorageService storageService;

    @BeforeEach
    void cleanDb() {
        skillRepository.deleteAll();

        Mockito.when(storageService.upload(Mockito.any()))
                .thenReturn(new StorageResult(java.util.UUID.randomUUID().toString(), Faker.instance().letterify("letter")));

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
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("React"));

        assertTrue(skillRepository.existsByName("React"));
    }
}
