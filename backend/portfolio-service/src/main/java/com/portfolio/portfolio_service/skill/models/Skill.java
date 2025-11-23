package com.portfolio.portfolio_service.skill.models;

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
@Table(name = "skills")
public class Skill {
    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private UUID skillId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String storageKey;
    @Column(nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;
}
