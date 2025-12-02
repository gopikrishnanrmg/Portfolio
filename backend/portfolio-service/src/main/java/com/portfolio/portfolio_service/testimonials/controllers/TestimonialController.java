package com.portfolio.portfolio_service.testimonials.controllers;

import com.portfolio.portfolio_service.testimonials.dtos.CreateTestimonialRequest;
import com.portfolio.portfolio_service.testimonials.dtos.TestimonialResponse;
import com.portfolio.portfolio_service.testimonials.dtos.UpdateTestimonialRequest;
import com.portfolio.portfolio_service.testimonials.services.TestimonialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/testimonials")
@RequiredArgsConstructor
public class TestimonialController {

    private final TestimonialService testimonialService;

    @PostMapping()
    public ResponseEntity<TestimonialResponse> createTestimonial(
            @Valid @RequestBody CreateTestimonialRequest testimonialRequest) {
        TestimonialResponse created = testimonialService.createTestimonial(testimonialRequest);
        URI location = URI.create("/api/v1/testimonials/" + created.testimonialId());
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping()
    public ResponseEntity<List<TestimonialResponse>> getAllTestimonials() {
        return ResponseEntity.ok().body(testimonialService.getAllTestimonials());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TestimonialResponse> updateTestimonial(
            @RequestBody UpdateTestimonialRequest testimonialRequest,
            @PathVariable UUID id) {
        return ResponseEntity.ok(testimonialService.updateTestimonial(id, testimonialRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        testimonialService.deleteTestimonial(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/hard/{id}")
    public ResponseEntity<Void> deleteByIdHard(@PathVariable UUID id) {
        testimonialService.deleteTestimonialHard(id);
        return ResponseEntity.noContent().build();
    }
}

