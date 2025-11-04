package com.portfolio.portfolio_service.skill.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
public class Skill {
    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private UUID skillId;
    private Category category;
    private String name;
    private String uri;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean isDeleted;
}
