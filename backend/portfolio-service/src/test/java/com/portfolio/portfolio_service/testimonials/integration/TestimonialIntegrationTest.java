package com.portfolio.portfolio_service.testimonials.integration;

import com.portfolio.portfolio_service.testimonials.models.Testimonial;
import com.portfolio.portfolio_service.testimonials.repositories.TestimonialRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TestimonialIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestimonialRepository testimonialRepository;

    @BeforeEach
    void cleanDb() { testimonialRepository.deleteAll(); }

    @Test
    void createTestimonial_shouldPersistAndReturnResponse() throws Exception {
        String requestJson = """
            {
              "name": "John Doe",
              "role": "Software Engineer",
              "text": "Text",
              "initials": "JD",
              "accent": "from-pink-500 via-purple-500 to-cyan-400"
            }
        """;

        mockMvc.perform(post("/api/v1/testimonials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.role").value("Software Engineer"))
                .andExpect(jsonPath("$.text").value("Text"));

        assertEquals(1, testimonialRepository.findAll().size());
    }

    @Test
    void getAllTestimonials_shouldReturnFiltered() throws Exception {
        Testimonial active = Testimonial.builder()
                .name("Active")
                .role("Engineer")
                .text("Active testimonial")
                .initials("AC")
                .accent("from-pink-500 via-purple-500 to-cyan-400")
                .isDeleted(false)
                .build();

        Testimonial deleted = Testimonial.builder()
                .name("Deleted")
                .role("Engineer")
                .text("Deleted testimonial")
                .initials("DC")
                .accent("from-pink-500 via-purple-500 to-cyan-400")
                .isDeleted(true)
                .build();

        testimonialRepository.saveAll(List.of(active, deleted));

        mockMvc.perform(get("/api/v1/testimonials"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Active"));
    }

    @Test
    void updateTestimonial_shouldUpdateFields() throws Exception {
        Testimonial testimonial = testimonialRepository.save(
                Testimonial.builder()
                        .name("Old Name")
                        .role("Old Role")
                        .text("Old Text")
                        .initials("ON")
                        .accent("old-accent")
                        .isDeleted(false)
                        .build()
        );

        String updateJson = """
            {
              "name": "New Name",
              "role": "New Role",
              "text": "Updated testimonial text",
              "initials": "NN",
              "accent": "new-accent"
            }
        """;

        mockMvc.perform(put("/api/v1/testimonials/" + testimonial.getTestimonialId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.role").value("New Role"))
                .andExpect(jsonPath("$.text").value("Updated testimonial text"));

        Testimonial updated = testimonialRepository.findById(testimonial.getTestimonialId()).orElseThrow();
        assertEquals("New Name", updated.getName());
        assertEquals("New Role", updated.getRole());
        assertEquals("Updated testimonial text", updated.getText());
    }

    @Test
    void deleteTestimonial_shouldMarkDeleted() throws Exception {
        Testimonial testimonial = testimonialRepository.save(
                Testimonial.builder()
                        .name("ToDelete")
                        .role("Engineer")
                        .text("Delete me")
                        .initials("TD")
                        .accent("accent")
                        .isDeleted(false)
                        .build()
        );

        mockMvc.perform(delete("/api/v1/testimonials/" + testimonial.getTestimonialId()))
                .andExpect(status().isNoContent());

        Testimonial deleted = testimonialRepository.findById(testimonial.getTestimonialId()).orElseThrow();
        assertTrue(deleted.isDeleted());
    }

    @Test
    void deleteTestimonialHard_shouldRemoveEntity() throws Exception {
        Testimonial testimonial = testimonialRepository.save(
                Testimonial.builder()
                        .name("HardDelete")
                        .role("Engineer")
                        .text("Hard delete me")
                        .initials("HD")
                        .accent("accent")
                        .isDeleted(false)
                        .build()
        );

        mockMvc.perform(delete("/api/v1/testimonials/hard/" + testimonial.getTestimonialId()))
                .andExpect(status().isNoContent());

        assertFalse(testimonialRepository.findById(testimonial.getTestimonialId()).isPresent());
    }
}

