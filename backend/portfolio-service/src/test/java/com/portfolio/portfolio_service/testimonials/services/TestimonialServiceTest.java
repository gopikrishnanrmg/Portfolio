package com.portfolio.portfolio_service.testimonials.services;

import com.portfolio.portfolio_service.testimonials.dtos.CreateTestimonialRequest;
import com.portfolio.portfolio_service.testimonials.dtos.TestimonialResponse;
import com.portfolio.portfolio_service.testimonials.dtos.UpdateTestimonialRequest;
import com.portfolio.portfolio_service.testimonials.exceptions.DuplicateTestimonialException;
import com.portfolio.portfolio_service.testimonials.exceptions.InvalidTestimonialUpdateException;
import com.portfolio.portfolio_service.testimonials.exceptions.TestimonialNotFoundException;
import com.portfolio.portfolio_service.testimonials.mappers.TestimonialMapper;
import com.portfolio.portfolio_service.testimonials.models.Testimonial;
import com.portfolio.portfolio_service.testimonials.repositories.TestimonialRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestimonialServiceTest {

    @Mock private TestimonialRepository testimonialRepository;
    @Mock private TestimonialMapper testimonialMapper;

    @InjectMocks private TestimonialService testimonialService;

    private final UUID testimonialId = UUID.randomUUID();

    @Test
    void createTestimonial_shouldSaveAndReturnResponse() {
        CreateTestimonialRequest request = new CreateTestimonialRequest("John Doe", "Engineer", "Text", "JD", "accent");
        Testimonial testimonial = new Testimonial(testimonialId, "John Doe", "Engineer", "Text", "JD", "accent", false);
        TestimonialResponse expectedResponse = new TestimonialResponse(testimonialId, "John Doe", "Engineer", "Text", "JD", "accent");

        when(testimonialRepository.existsByNameAndIsDeletedFalse("John Doe")).thenReturn(false);
        when(testimonialMapper.testimonialRequestToTestimonial(request)).thenReturn(testimonial);
        when(testimonialRepository.save(testimonial)).thenReturn(testimonial);
        when(testimonialMapper.testimonialToTestimonialResponse(testimonial)).thenReturn(expectedResponse);

        TestimonialResponse actualResponse = testimonialService.createTestimonial(request);

        assertEquals(expectedResponse, actualResponse);
        verify(testimonialRepository).existsByNameAndIsDeletedFalse("John Doe");
        verify(testimonialMapper).testimonialRequestToTestimonial(request);
        verify(testimonialRepository).save(testimonial);
        verify(testimonialMapper).testimonialToTestimonialResponse(testimonial);
    }

    @Test
    void createTestimonial_shouldThrowDuplicateTestimonialException() {
        CreateTestimonialRequest request = new CreateTestimonialRequest("John Doe", "Engineer", "Text", "JD", "accent");
        when(testimonialRepository.existsByNameAndIsDeletedFalse("John Doe")).thenReturn(true);

        assertThrows(DuplicateTestimonialException.class, () -> testimonialService.createTestimonial(request));

        verify(testimonialRepository).existsByNameAndIsDeletedFalse("John Doe");
        verifyNoMoreInteractions(testimonialRepository, testimonialMapper);
    }

    @Test
    void getAllTestimonials_shouldFilterDeleted() {
        Testimonial active = new Testimonial(testimonialId, "Active", "Engineer", "Text", "AC", "accent", false);
        Testimonial deleted = new Testimonial(UUID.randomUUID(), "Deleted", "Engineer", "Text", "DC", "accent", true);

        TestimonialResponse activeResponse = new TestimonialResponse(testimonialId, "Active", "Engineer", "Text", "AC", "accent");

        when(testimonialRepository.findAll()).thenReturn(List.of(active, deleted));
        when(testimonialMapper.testimonialToTestimonialResponse(active)).thenReturn(activeResponse);

        List<TestimonialResponse> result = testimonialService.getAllTestimonials();

        assertEquals(1, result.size());
        assertEquals("Active", result.get(0).name());

        verify(testimonialRepository).findAll();
        verify(testimonialMapper).testimonialToTestimonialResponse(active);
        verify(testimonialMapper, never()).testimonialToTestimonialResponse(deleted);
    }

    @Test
    void updateTestimonial_shouldReturnUpdatedResponse() {
        UpdateTestimonialRequest request = new UpdateTestimonialRequest("Jane Doe", "Manager", "Updated text", "JD", "new-accent");
        Testimonial testimonial = new Testimonial(testimonialId, "John Doe", "Engineer", "Old text", "JD", "accent", false);
        TestimonialResponse response = new TestimonialResponse(testimonialId, "Jane Doe", "Manager", "Updated text", "JD", "new-accent");

        when(testimonialRepository.findById(testimonialId)).thenReturn(Optional.of(testimonial));
        when(testimonialRepository.save(testimonial)).thenReturn(testimonial);
        when(testimonialMapper.testimonialToTestimonialResponse(testimonial)).thenReturn(response);

        TestimonialResponse result = testimonialService.updateTestimonial(testimonialId, request);

        assertEquals("Jane Doe", result.name());
        assertEquals("Updated text", result.text());

        verify(testimonialRepository).findById(testimonialId);
        verify(testimonialRepository).save(testimonial);
        verify(testimonialMapper).testimonialToTestimonialResponse(testimonial);
    }

    @Test
    void updateTestimonial_shouldThrowTestimonialNotFoundException() {
        UpdateTestimonialRequest request = new UpdateTestimonialRequest("Jane Doe", null, null, null, null);
        when(testimonialRepository.findById(testimonialId)).thenReturn(Optional.empty());

        assertThrows(TestimonialNotFoundException.class, () -> testimonialService.updateTestimonial(testimonialId, request));

        verify(testimonialRepository).findById(testimonialId);
        verifyNoMoreInteractions(testimonialRepository, testimonialMapper);
    }

    @Test
    void updateTestimonial_shouldThrowInvalidTestimonialUpdateException() {
        UpdateTestimonialRequest request = new UpdateTestimonialRequest("", null, null, null, null);
        Testimonial testimonial = new Testimonial(testimonialId, "John Doe", "Engineer", "Old text", "JD", "accent", false);

        when(testimonialRepository.findById(testimonialId)).thenReturn(Optional.of(testimonial));

        assertThrows(InvalidTestimonialUpdateException.class, () -> testimonialService.updateTestimonial(testimonialId, request));

        verify(testimonialRepository).findById(testimonialId);
        verifyNoMoreInteractions(testimonialRepository, testimonialMapper);
    }

    @Test
    void deleteTestimonial_shouldMarkDeleted() {
        Testimonial testimonial = new Testimonial(testimonialId, "John Doe", "Engineer", "Text", "JD", "accent", false);
        when(testimonialRepository.findById(testimonialId)).thenReturn(Optional.of(testimonial));

        testimonialService.deleteTestimonial(testimonialId);

        assertTrue(testimonial.isDeleted());
        verify(testimonialRepository).findById(testimonialId);
        verify(testimonialRepository).save(testimonial);
    }

    @Test
    void deleteTestimonial_shouldThrowTestimonialNotFoundException() {
        when(testimonialRepository.findById(testimonialId)).thenReturn(Optional.empty());

        assertThrows(TestimonialNotFoundException.class, () -> testimonialService.deleteTestimonial(testimonialId));

        verify(testimonialRepository).findById(testimonialId);
        verifyNoMoreInteractions(testimonialRepository, testimonialMapper);
    }

    @Test
    void deleteTestimonialHard_shouldDelete() {
        Testimonial testimonial = new Testimonial(testimonialId, "John Doe", "Engineer", "Text", "JD", "accent", false);
        when(testimonialRepository.findById(testimonialId)).thenReturn(Optional.of(testimonial));

        testimonialService.deleteTestimonialHard(testimonialId);

        verify(testimonialRepository).findById(testimonialId);
        verify(testimonialRepository).delete(testimonial);
    }

    @Test
    void deleteTestimonialHard_shouldThrowTestimonialNotFoundException() {
        when(testimonialRepository.findById(testimonialId)).thenReturn(Optional.empty());

        assertThrows(TestimonialNotFoundException.class, () -> testimonialService.deleteTestimonialHard(testimonialId));

        verify(testimonialRepository).findById(testimonialId);
        verifyNoMoreInteractions(testimonialRepository, testimonialMapper);
    }
}
