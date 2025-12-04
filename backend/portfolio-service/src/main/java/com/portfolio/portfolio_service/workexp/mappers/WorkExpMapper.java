package com.portfolio.portfolio_service.workexp.mappers;

import com.portfolio.portfolio_service.workexp.dtos.CreateWorkExpRequest;
import com.portfolio.portfolio_service.workexp.dtos.WorkExpResponse;
import com.portfolio.portfolio_service.workexp.models.WorkExp;
import org.springframework.stereotype.Component;

@Component
public class WorkExpMapper {
    public WorkExpResponse workExpToWorkExpResponse(WorkExp workExp) {
        return new WorkExpResponse(workExp.getWorkExpId(), workExp.getRole(), workExp.getCompany(), workExp.getNote(), workExp.getStartDate(), workExp.getEndDate(), workExp.getPoints());
    }

    public WorkExp workExpRequestToWorkExp(CreateWorkExpRequest workExpRequest) {
        return WorkExp
                .builder()
                .role(workExpRequest.role())
                .company(workExpRequest.company())
                .note(workExpRequest.note())
                .startDate(workExpRequest.startDate())
                .endDate(workExpRequest.endDate())
                .points(workExpRequest.points())
                .build();
    }
}
