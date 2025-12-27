package com.portfolio.portfolio_service.workexp.services;

import com.portfolio.portfolio_service.workexp.dtos.CreateWorkExpRequest;
import com.portfolio.portfolio_service.workexp.dtos.ReplaceWorkExpRequest;
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

import java.time.LocalDate;
import java.util.ArrayList;
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
            if (workExpRequest.role().isNull())
                throw new InvalidWorkExpUpdateException("Role cannot be null");
            String value = workExpRequest.role().asText();
            if (value.isBlank())
                throw new InvalidWorkExpUpdateException("Role cannot be blank");
            workExp.setRole(value);
        }

        if (workExpRequest.company() != null) {
            if (workExpRequest.company().isNull())
                throw new InvalidWorkExpUpdateException("Company cannot be null");
            String value = workExpRequest.company().asText();
            if (value.isBlank())
                throw new InvalidWorkExpUpdateException("Company cannot be blank");
            workExp.setCompany(value);
        }

        if (workExpRequest.note() != null) {
            if (workExpRequest.note().isNull())
                workExp.setNote(null);
            else
                workExp.setNote(workExpRequest.note().asText());
        }

        if (workExpRequest.startDate() != null) {
            if (workExpRequest.startDate().isNull())
                throw new InvalidWorkExpUpdateException("startDate cannot be null");
            workExp.setStartDate(LocalDate.parse(workExpRequest.startDate().asText()));
        }

        if (workExpRequest.endDate() != null) {
            if (workExpRequest.endDate().isNull())
                workExp.setEndDate(null);
            else
                workExp.setEndDate(LocalDate.parse(workExpRequest.endDate().asText()));
        }

        if (workExpRequest.points() != null) {
            if (workExpRequest.points().isNull())
                throw new InvalidWorkExpUpdateException("points cannot be null");

            List<String> pts = new ArrayList<>();
            workExpRequest.points().forEach(node -> pts.add(node.asText()));

            if (pts.isEmpty())
                throw new InvalidWorkExpUpdateException("points cannot be empty");

            workExp.setPoints(pts);
        }

        WorkExp updated = workExpRepository.save(workExp);
        return workExpMapper.workExpToWorkExpResponse(updated);
    }

    public WorkExpResponse replaceWorkExp(UUID id, ReplaceWorkExpRequest request) {
        WorkExp workExp = workExpRepository.findById(id)
                .orElseThrow(() -> new WorkExpNotFoundException("WorkExp not found with id: " + id));

        workExp.setRole(request.role());
        workExp.setCompany(request.company());
        workExp.setNote(request.note());
        workExp.setStartDate(request.startDate());
        workExp.setEndDate(request.endDate());
        workExp.setPoints(request.points());

        WorkExp saved = workExpRepository.save(workExp);
        return workExpMapper.workExpToWorkExpResponse(saved);
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
