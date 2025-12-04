package com.portfolio.portfolio_service.workexp.dtos;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public record UpdateWorkExpRequest(String role, String company, Optional<String> note, LocalDate startDate, Optional<LocalDate> endDate, List<String> points) {
}
