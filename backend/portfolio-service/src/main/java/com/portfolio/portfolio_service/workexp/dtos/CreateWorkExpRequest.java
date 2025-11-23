package com.portfolio.portfolio_service.workexp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record CreateWorkExpRequest(@NotBlank String role, @NotBlank String company, @NotNull LocalDate startDate, LocalDate endDate, List<String> points) {
}
