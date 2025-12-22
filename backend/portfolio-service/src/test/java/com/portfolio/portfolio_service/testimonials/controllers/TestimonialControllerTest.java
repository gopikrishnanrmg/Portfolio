package com.portfolio.portfolio_service.testimonials.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.portfolio_service.testimonials.dtos.CreateTestimonialRequest;
import com.portfolio.portfolio_service.testimonials.dtos.TestimonialResponse;
import com.portfolio.portfolio_service.testimonials.dtos.UpdateTestimonialRequest;
import com.portfolio.portfolio_service.testimonials.dtos.ReplaceTestimonialRequest;
import com.portfolio.portfolio_service.testimonials.services.TestimonialService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TestimonialController.class)
@RequiredArgsConstructor
class TestimonialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TestimonialService testimonialService;

    @Autowired
    private ObjectMapper objectMapper;

    private final UUID testimonialId = UUID.randomUUID();

    @Test
    void createTestimonial_shouldReturnCreated() throws Exception {
        CreateTestimonialRequest request = new CreateTestimonialRequest(
                "John Doe",
                "Software Engineer",
                "Text",
                "JD",
                "from-pink-500 via-purple-500 to-cyan-400"
        );

        TestimonialResponse response = new TestimonialResponse(
                testimonialId,
                "John Doe",
                "Software Engineer",
                "Text",
                "JD",
                "from-pink-500 via-purple-500 to-cyan-400"
        );

        when(testimonialService.createTestimonial(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/testimonials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/testimonials/" + testimonialId))
                .andExpect(jsonPath("$.testimonialId").value(testimonialId.toString()))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.role").value("Software Engineer"))
                .andExpect(jsonPath("$.text").value("Text"));
    }

    @Test
    void createTestimonial_shouldThrowDTOValidationError() throws Exception {
        CreateTestimonialRequest request = new CreateTestimonialRequest(
                "",
                "Software Engineer",
                "Text",
                "JD",
                "from-pink-500 via-purple-500 to-cyan-400"
        );

        mockMvc.perform(post("/api/v1/testimonials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Error"))
                .andExpect(jsonPath("$.message").value(containsString("must not be blank")));
    }

    @Test
    void getAllTestimonials_shouldReturnList() throws Exception {
        TestimonialResponse response = new TestimonialResponse(
                testimonialId,
                "John Doe",
                "Software Engineer",
                "Text",
                "JD",
                "from-pink-500 via-purple-500 to-cyan-400"
        );

        when(testimonialService.getAllTestimonials()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/testimonials"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].testimonialId").value(testimonialId.toString()))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].role").value("Software Engineer"))
                .andExpect(jsonPath("$[0].text").value("Text"));
    }

    @Test
    void updateTestimonial_shouldReturnUpdated() throws Exception {
        String patchJson = """
        {
            "name": "Jane Doe",
            "role": "Product Manager",
            "text": "Updated testimonial text",
            "initials": "JD",
            "accent": "from-fuchsia-500 via-rose-400 to-amber-400"
        }
    """;

        TestimonialResponse response = new TestimonialResponse(
                testimonialId,
                "Jane Doe",
                "Product Manager",
                "Updated testimonial text",
                "JD",
                "from-fuchsia-500 via-rose-400 to-amber-400"
        );

        when(testimonialService.updateTestimonial(eq(testimonialId), any())).thenReturn(response);

        mockMvc.perform(patch("/api/v1/testimonials/" + testimonialId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.testimonialId").value(testimonialId.toString()))
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(jsonPath("$.role").value("Product Manager"))
                .andExpect(jsonPath("$.text").value("Updated testimonial text"));

        verify(testimonialService).updateTestimonial(eq(testimonialId), any());
    }

    @Test
    void replaceTestimonial_shouldReturnUpdated() throws Exception {
        ReplaceTestimonialRequest request = new ReplaceTestimonialRequest(
                "New Name",
                "New Role",
                "New Text",
                "NN",
                "new-accent"
        );

        TestimonialResponse response = new TestimonialResponse(
                testimonialId,
                "New Name",
                "New Role",
                "New Text",
                "NN",
                "new-accent"
        );

        when(testimonialService.replaceTestimonial(eq(testimonialId), any())).thenReturn(response);

        mockMvc.perform(put("/api/v1/testimonials/" + testimonialId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.testimonialId").value(testimonialId.toString()))
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.role").value("New Role"))
                .andExpect(jsonPath("$.text").value("New Text"));

        verify(testimonialService).replaceTestimonial(eq(testimonialId), any());
    }

    @Test
    void replaceTestimonial_shouldReturnBadRequestForInvalidDTO() throws Exception {
        ReplaceTestimonialRequest request = new ReplaceTestimonialRequest(
                "",
                "Role",
                "Text",
                "JD",
                "accent"
        );

        mockMvc.perform(put("/api/v1/testimonials/" + testimonialId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Error"))
                .andExpect(jsonPath("$.message").value(containsString("must not be blank")));
    }

    @Test
    void deleteTestimonial_shouldReturnNoContent() throws Exception {
        doNothing().when(testimonialService).deleteTestimonial(testimonialId);

        mockMvc.perform(delete("/api/v1/testimonials/" + testimonialId))
                .andExpect(status().isNoContent());

        verify(testimonialService).deleteTestimonial(testimonialId);
    }

    @Test
    void deleteTestimonialHard_shouldReturnNoContent() throws Exception {
        doNothing().when(testimonialService).deleteTestimonialHard(testimonialId);

        mockMvc.perform(delete("/api/v1/testimonials/hard/" + testimonialId))
                .andExpect(status().isNoContent());

        verify(testimonialService).deleteTestimonialHard(testimonialId);
    }
}