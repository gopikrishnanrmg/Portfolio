package com.portfolio.portfolio_service.testimonials.services;

import com.portfolio.portfolio_service.testimonials.dtos.CreateTestimonialRequest;
import com.portfolio.portfolio_service.testimonials.dtos.ReplaceTestimonialRequest;
import com.portfolio.portfolio_service.testimonials.dtos.TestimonialResponse;
import com.portfolio.portfolio_service.testimonials.dtos.UpdateTestimonialRequest;
import com.portfolio.portfolio_service.testimonials.exceptions.DuplicateTestimonialException;
import com.portfolio.portfolio_service.testimonials.exceptions.InvalidTestimonialUpdateException;
import com.portfolio.portfolio_service.testimonials.exceptions.TestimonialNotFoundException;
import com.portfolio.portfolio_service.testimonials.mappers.TestimonialMapper;
import com.portfolio.portfolio_service.testimonials.models.Testimonial;
import com.portfolio.portfolio_service.testimonials.repositories.TestimonialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TestimonialService {

    private final TestimonialRepository testimonialRepository;
    private final TestimonialMapper testimonialMapper;

    public TestimonialResponse createTestimonial(CreateTestimonialRequest testimonialRequest) {
        if (testimonialRepository.existsByNameAndIsDeletedFalse(testimonialRequest.name()))
            throw new DuplicateTestimonialException("Testimonial with this name already exists");

        Testimonial testimonial = testimonialMapper.testimonialRequestToTestimonial(testimonialRequest);
        Testimonial saved = testimonialRepository.save(testimonial);
        return testimonialMapper.testimonialToTestimonialResponse(saved);
    }

    public List<TestimonialResponse> getAllTestimonials() {
        return testimonialRepository.findAll()
                .stream()
                .filter(testimonial -> !testimonial.isDeleted())
                .map(testimonialMapper::testimonialToTestimonialResponse)
                .toList();
    }

    public TestimonialResponse updateTestimonial(UUID testimonialId, UpdateTestimonialRequest testimonialRequest) {
        Testimonial testimonial = testimonialRepository.findById(testimonialId)
                .orElseThrow(() -> new TestimonialNotFoundException("Testimonial not found with id: " + testimonialId));

        if (testimonialRequest.name() != null) {
            if (testimonialRequest.name().isNull())
                throw new InvalidTestimonialUpdateException("Name cannot be null");
            String value = testimonialRequest.name().asText();
            if (value.isBlank())
                throw new InvalidTestimonialUpdateException("Name cannot be blank");
            testimonial.setName(value);
        }

        if (testimonialRequest.role() != null) {
            if (testimonialRequest.role().isNull())
                throw new InvalidTestimonialUpdateException("Role cannot be null");
            String value = testimonialRequest.role().asText();
            if (value.isBlank())
                throw new InvalidTestimonialUpdateException("Role cannot be blank");
            testimonial.setRole(value);
        }

        if (testimonialRequest.text() != null) {
            if (testimonialRequest.text().isNull())
                throw new InvalidTestimonialUpdateException("Text cannot be null");
            String value = testimonialRequest.text().asText();
            if (value.isBlank())
                throw new InvalidTestimonialUpdateException("Text cannot be blank");
            testimonial.setText(value);
        }

        if (testimonialRequest.initials() != null) {
            if (testimonialRequest.initials().isNull())
                throw new InvalidTestimonialUpdateException("Initials cannot be null");
            String value = testimonialRequest.initials().asText();
            if (value.isBlank())
                throw new InvalidTestimonialUpdateException("Initials cannot be blank");
            testimonial.setInitials(value);
        }

        if (testimonialRequest.accent() != null) {
            if (testimonialRequest.accent().isNull())
                throw new InvalidTestimonialUpdateException("Accent cannot be null");
            String value = testimonialRequest.accent().asText();
            if (value.isBlank())
                throw new InvalidTestimonialUpdateException("Accent cannot be blank");
            testimonial.setAccent(value);
        }

        Testimonial updated = testimonialRepository.save(testimonial);
        return testimonialMapper.testimonialToTestimonialResponse(updated);
    }

    public TestimonialResponse replaceTestimonial(UUID id, ReplaceTestimonialRequest request) {
        Testimonial testimonial = testimonialRepository.findById(id)
                .orElseThrow(() -> new TestimonialNotFoundException("Testimonial not found with id: " + id));

        testimonial.setName(request.name());
        testimonial.setRole(request.role());
        testimonial.setText(request.text());
        testimonial.setInitials(request.initials());
        testimonial.setAccent(request.accent());

        Testimonial saved = testimonialRepository.save(testimonial);
        return testimonialMapper.testimonialToTestimonialResponse(saved);
    }

    public void deleteTestimonial(UUID testimonialId) {
        Testimonial testimonial = testimonialRepository.findById(testimonialId)
                .orElseThrow(() -> new TestimonialNotFoundException("Testimonial not found with id: " + testimonialId));

        testimonial.setDeleted(true);
        testimonialRepository.save(testimonial);
    }

    public void deleteTestimonialHard(UUID testimonialId) {
        Testimonial testimonial = testimonialRepository.findById(testimonialId)
                .orElseThrow(() -> new TestimonialNotFoundException("Testimonial not found with id: " + testimonialId));

        testimonialRepository.delete(testimonial);
    }
}
