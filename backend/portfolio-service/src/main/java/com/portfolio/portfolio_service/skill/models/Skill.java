package com.portfolio.portfolio_service.skill.models;

import com.portfolio.portfolio_service.skill.storage.dtos.StorageResult;
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
public class Skill {
    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private UUID skillId;
    @Enumerated(EnumType.STRING)
    private Category category;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String storageKey;
    @Column(nullable = false, columnDefinition = "boolean default false")
    @Builder.Default
    private Boolean isDeleted = false;
}
