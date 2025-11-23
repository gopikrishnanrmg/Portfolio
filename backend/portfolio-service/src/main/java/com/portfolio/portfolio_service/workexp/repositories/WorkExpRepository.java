package com.portfolio.portfolio_service.workexp.repositories;

import com.portfolio.portfolio_service.workexp.models.WorkExp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface WorkExpRepository extends JpaRepository<WorkExp, UUID> {
    boolean existsByRoleAndCompanyAndStartDate(String role, String company, LocalDate startDate);
}
