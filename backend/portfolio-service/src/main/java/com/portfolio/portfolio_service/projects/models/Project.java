package com.portfolio.portfolio_service.projects.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private UUID projectId;
    @Column(nullable = false, unique = true)
    private String title;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;
    @ElementCollection(fetch = FetchType.EAGER)
    @OrderColumn(name = "position")
    @NotEmpty
    private List<String> tech;
    @Column(nullable = false)
    private String banner;
    @Column(unique = true)
    private String link;
    @Column(nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;
}
