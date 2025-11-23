package com.portfolio.portfolio_service.workexp.dtos;

import java.time.LocalDate;
import java.util.UUID;
import java.util.List;

public record WorkExpResponse(UUID workExpId, String role, String company, LocalDate startDate, LocalDate endDate, List<String> points) {
}