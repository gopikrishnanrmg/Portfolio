package com.portfolio.portfolio_service.workexp.controllers;

import com.portfolio.portfolio_service.workexp.dtos.CreateWorkExpRequest;
import com.portfolio.portfolio_service.workexp.dtos.UpdateWorkExpRequest;
import com.portfolio.portfolio_service.workexp.dtos.WorkExpResponse;
import com.portfolio.portfolio_service.workexp.services.WorkExpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/workexps")
@RequiredArgsConstructor
public class WorkExpController {

    private final WorkExpService workExpService;

    @PostMapping()
    public ResponseEntity<WorkExpResponse> createWorkExp(
            @Valid @RequestBody CreateWorkExpRequest workExpRequest){
        WorkExpResponse created = workExpService.createWorkExp(workExpRequest);
        URI location = URI.create("/api/v1/workexps/" + created.workExpId());
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping()
    public ResponseEntity<List<WorkExpResponse>> getAllWorkExps() {
        return ResponseEntity.ok().body(workExpService.getAllWorkExps());
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkExpResponse> updateSkill(
            @RequestBody UpdateWorkExpRequest workExpRequest,
            @PathVariable UUID id) {
        return ResponseEntity.ok(workExpService.updateWorkExp(id, workExpRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        workExpService.deleteWorkExp(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/hard/{id}")
    public ResponseEntity<Void> deleteByIdHard(@PathVariable UUID id) {
        workExpService.deleteWorkExpHard(id);
        return ResponseEntity.noContent().build();
    }
}
