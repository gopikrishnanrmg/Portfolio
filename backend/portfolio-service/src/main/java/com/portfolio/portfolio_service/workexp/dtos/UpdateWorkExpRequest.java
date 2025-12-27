package com.portfolio.portfolio_service.workexp.dtos;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public record UpdateWorkExpRequest(JsonNode role, JsonNode company, JsonNode note, JsonNode startDate, JsonNode endDate, JsonNode points){
}
