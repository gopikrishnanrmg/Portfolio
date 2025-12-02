package com.portfolio.portfolio_service.testimonials.mappers;

import com.portfolio.portfolio_service.testimonials.dtos.CreateTestimonialRequest;
import com.portfolio.portfolio_service.testimonials.dtos.TestimonialResponse;
import com.portfolio.portfolio_service.testimonials.models.Testimonial;
import org.springframework.stereotype.Component;

@Component
public class TestimonialMapper {
    public TestimonialResponse testimonialToTestimonialResponse(Testimonial testimonial) {
        return new TestimonialResponse(testimonial.getTestimonialId(), testimonial.getName(), testimonial.getRole(), testimonial.getText(), testimonial.getInitials(), testimonial.getAccent());
    }

    public Testimonial testimonialRequestToTestimonial(CreateTestimonialRequest testimonialRequest) {
        return Testimonial
                .builder()
                .name(testimonialRequest.name())
                .role(testimonialRequest.role())
                .text(testimonialRequest.text())
                .initials(testimonialRequest.initials())
                .accent(testimonialRequest.accent())
                .build();
    }
}
