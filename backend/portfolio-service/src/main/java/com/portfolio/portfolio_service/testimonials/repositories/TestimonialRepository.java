package com.portfolio.portfolio_service.testimonials.repositories;

import com.portfolio.portfolio_service.testimonials.models.Testimonial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TestimonialRepository extends JpaRepository<Testimonial, UUID>{
        boolean existsByName(String name);
}
