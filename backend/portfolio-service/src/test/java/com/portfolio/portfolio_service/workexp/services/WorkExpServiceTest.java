package com.portfolio.portfolio_service.workexp.services;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkExpServiceTest {

    @Mock private WorkExpRepository workExpRepository;
    @Mock private WorkExpMapper workExpMapper;

    @InjectMocks private WorkExpService workExpService;

    private final UUID workExpId = UUID.randomUUID();

    @Test
    void createWorkExp_shouldSaveAndReturnResponse() {
        CreateWorkExpRequest request = new CreateWorkExpRequest("Developer", "CompanyX", null, LocalDate.of(2020,1,1), null, List.of("Did stuff"));
        WorkExp workExp = WorkExp.builder()
                .workExpId(workExpId)
                .role("Developer")
                .company("CompanyX")
                .startDate(LocalDate.of(2020,1,1))
                .build();

        WorkExpResponse expectedResponse = new WorkExpResponse(workExpId, "Developer", "CompanyX", null, LocalDate.of(2020,1,1), null, List.of("Did stuff"));

        when(workExpRepository.existsByRoleAndCompanyAndStartDateAndIsDeletedFalse("Developer","CompanyX", LocalDate.of(2020,1,1))).thenReturn(false);
        when(workExpMapper.workExpRequestToWorkExp(request)).thenReturn(workExp);
        when(workExpRepository.save(workExp)).thenReturn(workExp);
        when(workExpMapper.workExpToWorkExpResponse(workExp)).thenReturn(expectedResponse);

        WorkExpResponse actualResponse = workExpService.createWorkExp(request);

        assertEquals(expectedResponse, actualResponse);

        verify(workExpRepository).existsByRoleAndCompanyAndStartDateAndIsDeletedFalse("Developer","CompanyX", LocalDate.of(2020,1,1));
        verify(workExpMapper).workExpRequestToWorkExp(request);
        verify(workExpRepository).save(workExp);
        verify(workExpMapper).workExpToWorkExpResponse(workExp);
    }

    @Test
    void createWorkExp_shouldThrowDuplicateWorkExpException() {
        CreateWorkExpRequest request = new CreateWorkExpRequest("Developer", "CompanyX", null, LocalDate.of(2020,1,1), null, List.of("Did stuff"));

        when(workExpRepository.existsByRoleAndCompanyAndStartDateAndIsDeletedFalse("Developer","CompanyX", LocalDate.of(2020,1,1))).thenReturn(true);

        assertThrows(DuplicateWorkExpException.class, () -> workExpService.createWorkExp(request));

        verify(workExpRepository).existsByRoleAndCompanyAndStartDateAndIsDeletedFalse("Developer","CompanyX", LocalDate.of(2020,1,1));
        verifyNoMoreInteractions(workExpRepository, workExpMapper);
    }

    @Test
    void getAllWorkExps_shouldFilterDeletedAndSort() {
        WorkExp older = new WorkExp(UUID.randomUUID(), "Older", "CompanyA", "NoteA",
                LocalDate.of(2019,1,1), null, List.of("Point"), false);
        WorkExp newer = new WorkExp(workExpId, "Newer", "CompanyB", "NoteB",
                LocalDate.of(2021,1,1), null, List.of("Point"), false);
        WorkExp deleted = new WorkExp(UUID.randomUUID(), "Deleted", "CompanyC", null,
                LocalDate.of(2020,1,1), null, List.of("Point"), true);

        WorkExpResponse olderResponse = new WorkExpResponse(older.getWorkExpId(), "Older", "CompanyA", "NoteA",
                LocalDate.of(2019,1,1), null, List.of("Point"));
        WorkExpResponse newerResponse = new WorkExpResponse(workExpId, "Newer", "CompanyB", "NoteB",
                LocalDate.of(2021,1,1), null, List.of("Point"));

        when(workExpRepository.findAll()).thenReturn(List.of(older, newer, deleted));
        when(workExpMapper.workExpToWorkExpResponse(older)).thenReturn(olderResponse);
        when(workExpMapper.workExpToWorkExpResponse(newer)).thenReturn(newerResponse);

        List<WorkExpResponse> result = workExpService.getAllWorkExps();

        assertEquals(2, result.size());
        assertFalse(result.stream().anyMatch(r -> r.role().equals("Deleted")));

        assertEquals("Older", result.get(0).role());
        assertEquals("Newer", result.get(1).role());

        verify(workExpRepository).findAll();
        verify(workExpMapper).workExpToWorkExpResponse(older);
        verify(workExpMapper).workExpToWorkExpResponse(newer);
        verify(workExpMapper, never()).workExpToWorkExpResponse(deleted);
    }

    @Test
    void updateWorkExp_shouldReturnUpdatedResponse() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        UpdateWorkExpRequest request = new UpdateWorkExpRequest(
                mapper.readTree("\"Engineer\""),
                mapper.readTree("\"CompanyY\""),
                mapper.nullNode(),
                mapper.readTree("\"2022-01-01\""),
                mapper.nullNode(),
                mapper.readTree("[\"Updated\"]")
        );

        WorkExp workExp = new WorkExp(
                workExpId, "Dev", "CompanyX", null,
                LocalDate.of(2020,1,1), null,
                List.of("Old"), false
        );

        WorkExpResponse response = new WorkExpResponse(
                workExpId, "Engineer", "CompanyY", null,
                LocalDate.of(2022,1,1), null,
                List.of("Updated")
        );

        when(workExpRepository.findById(workExpId)).thenReturn(Optional.of(workExp));
        when(workExpRepository.save(workExp)).thenReturn(workExp);
        when(workExpMapper.workExpToWorkExpResponse(workExp)).thenReturn(response);

        WorkExpResponse result = workExpService.updateWorkExp(workExpId, request);

        assertEquals("Engineer", result.role());
        assertEquals("CompanyY", result.company());

        verify(workExpRepository).findById(workExpId);
        verify(workExpRepository).save(workExp);
        verify(workExpMapper).workExpToWorkExpResponse(workExp);
    }

    @Test
    void updateWorkExp_shouldThrowWorkExpNotFoundException() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        UpdateWorkExpRequest request = new UpdateWorkExpRequest(
                mapper.readTree("\"Engineer\""),
                mapper.readTree("\"CompanyY\""),
                mapper.nullNode(),
                mapper.readTree("\"2022-01-01\""),
                mapper.nullNode(),
                mapper.readTree("[\"Updated\"]")
        );

        when(workExpRepository.findById(workExpId)).thenReturn(Optional.empty());

        assertThrows(WorkExpNotFoundException.class,
                () -> workExpService.updateWorkExp(workExpId, request));

        verify(workExpRepository).findById(workExpId);
        verifyNoMoreInteractions(workExpRepository, workExpMapper);
    }

    @Test
    void updateWorkExp_shouldThrowInvalidWorkExpUpdateException() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        UpdateWorkExpRequest request = new UpdateWorkExpRequest(
                mapper.readTree("\"\""),
                mapper.readTree("\"CompanyY\""),
                null,
                null,
                mapper.nullNode(),
                mapper.readTree("[\"Updated\"]")
        );

        WorkExp workExp = new WorkExp(
                workExpId, "Dev", "CompanyX", null,
                LocalDate.of(2020,1,1), null,
                List.of("Old"), false
        );

        when(workExpRepository.findById(workExpId)).thenReturn(Optional.of(workExp));

        assertThrows(InvalidWorkExpUpdateException.class,
                () -> workExpService.updateWorkExp(workExpId, request));

        verify(workExpRepository).findById(workExpId);
        verifyNoMoreInteractions(workExpRepository, workExpMapper);
    }

    @Test
    void replaceWorkExp_shouldReplaceAllFields() {
        ReplaceWorkExpRequest request = new ReplaceWorkExpRequest(
                "New Role",
                "New Company",
                "New note",
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2024, 1, 1),
                List.of("New point")
        );

        WorkExp workExp = new WorkExp(
                workExpId,
                "Old Role",
                "Old Company",
                "Old note",
                LocalDate.of(2020, 1, 1),
                null,
                List.of("Old point"),
                false
        );

        when(workExpRepository.findById(workExpId)).thenReturn(Optional.of(workExp));
        when(workExpRepository.save(workExp)).thenReturn(workExp);

        WorkExpResponse response = new WorkExpResponse(
                workExpId,
                "New Role",
                "New Company",
                "New note",
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2024, 1, 1),
                List.of("New point")
        );

        when(workExpMapper.workExpToWorkExpResponse(workExp)).thenReturn(response);

        WorkExpResponse result = workExpService.replaceWorkExp(workExpId, request);

        assertEquals("New Role", workExp.getRole());
        assertEquals("New Company", workExp.getCompany());
        assertEquals("New note", workExp.getNote());
        assertEquals(LocalDate.of(2023, 1, 1), workExp.getStartDate());
        assertEquals(LocalDate.of(2024, 1, 1), workExp.getEndDate());
        assertEquals(List.of("New point"), workExp.getPoints());
    }

    @Test
    void replaceWorkExp_shouldAllowNullOptionalFields() {
        ReplaceWorkExpRequest request = new ReplaceWorkExpRequest(
                "New Role",
                "New Company",
                null,
                LocalDate.of(2023, 1, 1),
                null,
                List.of("Point")
        );

        WorkExp workExp = new WorkExp(
                workExpId,
                "Old Role",
                "Old Company",
                "Old note",
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2021, 1, 1),
                List.of("Old point"),
                false
        );

        when(workExpRepository.findById(workExpId)).thenReturn(Optional.of(workExp));
        when(workExpRepository.save(workExp)).thenReturn(workExp);

        WorkExpResponse response = new WorkExpResponse(
                workExpId,
                "New Role",
                "New Company",
                null,
                LocalDate.of(2023, 1, 1),
                null,
                List.of("Point")
        );

        when(workExpMapper.workExpToWorkExpResponse(workExp)).thenReturn(response);

        WorkExpResponse result = workExpService.replaceWorkExp(workExpId, request);

        assertNull(workExp.getNote());
        assertNull(workExp.getEndDate());
    }

    @Test
    void replaceWorkExp_shouldThrowWorkExpNotFoundException() {
        ReplaceWorkExpRequest request = new ReplaceWorkExpRequest(
                "Role",
                "Company",
                "note",
                LocalDate.of(2023, 1, 1),
                null,
                List.of("Point")
        );

        when(workExpRepository.findById(workExpId)).thenReturn(Optional.empty());

        assertThrows(WorkExpNotFoundException.class,
                () -> workExpService.replaceWorkExp(workExpId, request));

        verify(workExpRepository).findById(workExpId);
        verifyNoMoreInteractions(workExpRepository, workExpMapper);
    }

    @Test
    void deleteWorkExp_shouldMarkDeleted() {
        WorkExp workExp = new WorkExp(workExpId, "Dev", "CompanyX", null, LocalDate.of(2020,1,1), null, List.of("Point"), false);

        when(workExpRepository.findById(workExpId)).thenReturn(Optional.of(workExp));

        workExpService.deleteWorkExp(workExpId);

        assertTrue(workExp.getIsDeleted());

        verify(workExpRepository).findById(workExpId);
        verify(workExpRepository).save(workExp);
    }

    @Test
    void deleteWorkExp_shouldThrowWorkExpNotFoundException() {
        when(workExpRepository.findById(workExpId)).thenReturn(Optional.empty());

        assertThrows(WorkExpNotFoundException.class, () -> workExpService.deleteWorkExp(workExpId));

        verify(workExpRepository).findById(workExpId);
        verifyNoMoreInteractions(workExpRepository, workExpMapper);
    }

    @Test
    void deleteWorkExpHard_shouldDelete() {
        WorkExp workExp = new WorkExp(workExpId, "Dev", "CompanyX", null, LocalDate.of(2020,1,1), null, List.of("Point"), false);

        when(workExpRepository.findById(workExpId)).thenReturn(Optional.of(workExp));

        workExpService.deleteWorkExpHard(workExpId);

        verify(workExpRepository).findById(workExpId);
        verify(workExpRepository).delete(workExp);
    }

    @Test
    void deleteWorkExpHard_shouldThrowWorkExpNotFoundException() {
        when(workExpRepository.findById(workExpId)).thenReturn(Optional.empty());

        assertThrows(WorkExpNotFoundException.class, () -> workExpService.deleteWorkExpHard(workExpId));

        verify(workExpRepository).findById(workExpId);
        verifyNoMoreInteractions(workExpRepository, workExpMapper);
    }

}
