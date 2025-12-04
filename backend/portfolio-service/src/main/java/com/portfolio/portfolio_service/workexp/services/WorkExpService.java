package com.portfolio.portfolio_service.workexp.services;

import com.portfolio.portfolio_service.workexp.dtos.CreateWorkExpRequest;
import com.portfolio.portfolio_service.workexp.dtos.UpdateWorkExpRequest;
import com.portfolio.portfolio_service.workexp.dtos.WorkExpResponse;
import com.portfolio.portfolio_service.workexp.exceptions.DuplicateWorkExpException;
import com.portfolio.portfolio_service.workexp.exceptions.InvalidWorkExpUpdateException;
import com.portfolio.portfolio_service.workexp.exceptions.WorkExpNotFoundException;
import com.portfolio.portfolio_service.workexp.mappers.WorkExpMapper;
import com.portfolio.portfolio_service.workexp.models.WorkExp;
import com.portfolio.portfolio_service.workexp.repositories.WorkExpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkExpService {

    private final WorkExpRepository workExpRepository;
    private final WorkExpMapper workExpMapper;

    public WorkExpResponse createWorkExp(CreateWorkExpRequest workExpRequest) {
        if (workExpRepository.existsByRoleAndCompanyAndStartDateAndIsDeletedFalse(workExpRequest.role(), workExpRequest.company(), workExpRequest.startDate())) {
            throw new DuplicateWorkExpException(
                    "Work experience already exists for role " + workExpRequest.role() +
                            ", company " + workExpRequest.company() +
                            ", and start date " + workExpRequest.startDate());
        }

        WorkExp workExp = workExpMapper.workExpRequestToWorkExp(workExpRequest);
        WorkExp saved = workExpRepository.save(workExp);
        return workExpMapper.workExpToWorkExpResponse(saved);
    }

    public List<WorkExpResponse> getAllWorkExps() {
        return workExpRepository.findAll()
                .stream()
                .filter(workExp -> !workExp.getIsDeleted())
                .map(workExpMapper::workExpToWorkExpResponse)
                .sorted(Comparator.comparing(WorkExpResponse::startDate))
                .toList();
    }

    public WorkExpResponse updateWorkExp(UUID workExpId, UpdateWorkExpRequest workExpRequest) {
        WorkExp workExp = workExpRepository.findById(workExpId)
                .orElseThrow(() -> new WorkExpNotFoundException("WorkExp not found with id: " + workExpId));

        if (workExpRequest.role() != null) {
            if (!workExpRequest.role().isBlank())
                workExp.setRole(workExpRequest.role());
            else
                throw new InvalidWorkExpUpdateException("Role cannot be blank");
        }

        if (workExpRequest.company() != null) {
            if (!workExpRequest.company().isBlank())
                workExp.setCompany(workExpRequest.company());
            else
                throw new InvalidWorkExpUpdateException("Company cannot be blank");
        }

        if (workExpRequest.note() != null) {
            if (workExpRequest.note().isPresent()) {
                workExp.setNote(workExpRequest.note().get());
            } else {
                workExp.setNote(null);
            }
        }

        if (workExpRequest.startDate() != null)
            workExp.setStartDate(workExpRequest.startDate());

        if (workExpRequest.endDate() != null) {
            if (workExpRequest.endDate().isPresent()) {
                workExp.setEndDate(workExpRequest.endDate().get());
            } else {
                workExp.setEndDate(null);
            }
        }

        if (workExpRequest.points() != null) {
            if (!workExpRequest.points().isEmpty())
                workExp.setPoints(workExpRequest.points());
            else
                throw new InvalidWorkExpUpdateException("Points cannot be empty");
        }

        WorkExp updated = workExpRepository.save(workExp);
        return workExpMapper.workExpToWorkExpResponse(updated);
    }

    public void deleteWorkExp(UUID workExpId) {
        WorkExp workExp = workExpRepository.findById(workExpId)
                .orElseThrow(() -> new WorkExpNotFoundException("WorkExp not found with id: " + workExpId));

        workExp.setIsDeleted(true);
        workExpRepository.save(workExp);
    }

    public void deleteWorkExpHard(UUID workExpId) {
        WorkExp workExp = workExpRepository.findById(workExpId)
                .orElseThrow(() -> new WorkExpNotFoundException("WorkExp not found with id: " + workExpId));

        workExpRepository.delete(workExp);
    }
}
