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
            if (!testimonialRequest.name().isBlank())
                testimonial.setName(testimonialRequest.name());
            else
                throw new InvalidTestimonialUpdateException("Name cannot be blank");
        }

        if (testimonialRequest.role() != null) {
            if (!testimonialRequest.role().isBlank())
                testimonial.setRole(testimonialRequest.role());
            else
                throw new InvalidTestimonialUpdateException("Role cannot be blank");
        }

        if (testimonialRequest.text() != null) {
            if (!testimonialRequest.text().isBlank())
                testimonial.setText(testimonialRequest.text());
            else
                throw new InvalidTestimonialUpdateException("Text cannot be blank");
        }

        if (testimonialRequest.initials() != null) {
            if (!testimonialRequest.initials().isBlank())
                testimonial.setInitials(testimonialRequest.initials());
            else
                throw new InvalidTestimonialUpdateException("Initials cannot be blank");
        }

        if (testimonialRequest.accent() != null) {
            if (!testimonialRequest.accent().isBlank())
                testimonial.setAccent(testimonialRequest.accent());
            else
                throw new InvalidTestimonialUpdateException("Accent cannot be blank");
        }


        Testimonial updated = testimonialRepository.save(testimonial);
        return testimonialMapper.testimonialToTestimonialResponse(updated);
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
