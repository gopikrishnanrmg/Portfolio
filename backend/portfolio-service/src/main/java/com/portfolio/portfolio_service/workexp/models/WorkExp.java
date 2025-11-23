package com.portfolio.portfolio_service.workexp.models;

import jakarta.persistence.*;
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
@Table(
        name = "workexps",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"role", "company", "startDate"})
        }
)
public class WorkExp {
    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private UUID workExpId;
    @Column(nullable = false)
    private String role;
    @Column(nullable = false)
    private String company;
    @Column(nullable = false)
    private LocalDate startDate;
    private LocalDate endDate;
    @ElementCollection
    @OrderColumn(name = "position")
    private List<String> points;
    @Column(nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;
}
