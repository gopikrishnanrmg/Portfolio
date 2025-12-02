package com.portfolio.portfolio_service.testimonials.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "testimonials")
public class Testimonial {
    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private UUID testimonialId;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String role;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;
    @Column(nullable = false)
    private String initials;
    @Column(nullable = false)
    private String accent;
    @Column(nullable = false)
    @Builder.Default
    private boolean isDeleted = false;
}
