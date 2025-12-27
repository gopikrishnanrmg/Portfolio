package com.portfolio.portfolio_service.workexp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record ReplaceWorkExpRequest(
        @NotBlank String role,
        @NotBlank String company,
        String note,
        @NotNull LocalDate startDate,
        LocalDate endDate,
        @NotEmpty List<String> points
) {}
