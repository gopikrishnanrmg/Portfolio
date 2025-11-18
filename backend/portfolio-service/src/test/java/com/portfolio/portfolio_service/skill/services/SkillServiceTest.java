package com.portfolio.portfolio_service.skill.services;

import com.portfolio.portfolio_service.blob_storage.StorageService;
import com.portfolio.portfolio_service.blob_storage.dtos.StorageResult;
import com.portfolio.portfolio_service.exceptions.FileProcessingException;
import com.portfolio.portfolio_service.skill.dtos.CreateSkillRequest;
import com.portfolio.portfolio_service.skill.dtos.SkillResponse;
import com.portfolio.portfolio_service.skill.dtos.UpdateSkillRequest;
import com.portfolio.portfolio_service.skill.exceptions.DuplicateSkillException;
import com.portfolio.portfolio_service.skill.exceptions.InvalidSkillUpdateException;
import com.portfolio.portfolio_service.skill.exceptions.SkillNotFoundException;
import com.portfolio.portfolio_service.skill.mappers.SkillMapper;
import com.portfolio.portfolio_service.skill.models.Category;
import com.portfolio.portfolio_service.skill.models.Skill;
import com.portfolio.portfolio_service.skill.repositories.SkillRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SkillServiceTest {

    @Mock private SkillRepository skillRepository;
    @Mock private StorageService storageService;
    @Mock private SkillMapper skillMapper;

    @InjectMocks private SkillService skillService;

    private final UUID skillId = UUID.randomUUID();

    @Test
    void createSkill_shouldThrowDuplicateSkillException() {
        CreateSkillRequest request = new CreateSkillRequest(Category.DEVELOPMENT, "React");
        MultipartFile file = mock(MultipartFile.class);

        when(skillRepository.existsByName("React")).thenReturn(true);

        assertThrows(DuplicateSkillException.class,
                () -> skillService.createSkill(request, file));

        verify(skillRepository).existsByName("React");
        verifyNoMoreInteractions(skillRepository, storageService, skillMapper);
    }

    @Test
    void createSkill_shouldThrowFileProcessingException() throws IOException {
        CreateSkillRequest request = new CreateSkillRequest(Category.DEVELOPMENT, "React");
        MultipartFile file = mock(MultipartFile.class);

        when(skillRepository.existsByName("React")).thenReturn(false);
        when(storageService.upload(file.getBytes())).thenThrow(new FileProcessingException("Upload failed"));

        assertThrows(FileProcessingException.class,
                () -> skillService.createSkill(request, file));
    }

    @Test
    void createSkill_shouldReturnSkillResponse() throws IOException {
        CreateSkillRequest request = new CreateSkillRequest(Category.DEVELOPMENT, "React");
        MultipartFile file = mock(MultipartFile.class);

        Skill skill = new Skill(skillId, Category.DEVELOPMENT, "React", "storage-key", false);
        StorageResult storageResult = new StorageResult("storage-key", "url");

        when(skillRepository.existsByName("React")).thenReturn(false);
        when(storageService.upload(file.getBytes())).thenReturn(storageResult);
        when(storageService.generatePresignedUrl(storageResult.key())).thenReturn("url");
        when(skillRepository.save(any(Skill.class))).thenReturn(skill);
        when(skillMapper.skillRequestToSkill(request, storageResult.key())).thenReturn(Skill.builder().name("React").category(Category.DEVELOPMENT).storageKey(storageResult.key()).build());
        when(skillMapper.skillToSkillResponse(skill, "url")).thenReturn(new SkillResponse(skillId, Category.DEVELOPMENT, "React", "url"));

        SkillResponse response = skillService.createSkill(request, file);

        assertEquals(skillId, response.skillId());
        assertEquals("React", response.name());
        assertEquals(Category.DEVELOPMENT, response.category());
        assertEquals("url", response.url());

        verify(skillRepository).existsByName("React");
        verify(storageService).upload(file.getBytes());
        verify(storageService).generatePresignedUrl("storage-key");
        verify(skillMapper).skillRequestToSkill(request, "storage-key");
        verify(skillMapper).skillToSkillResponse(skill, "url");
    }

    @Test
    void updateSkill_shouldThrowSkillNotFoundException() {
        UpdateSkillRequest request = new UpdateSkillRequest(Category.DEVELOPMENT, "ReactJS");

        when(skillRepository.findById(skillId)).thenReturn(Optional.empty());

        assertThrows(SkillNotFoundException.class,
                () -> skillService.updateSkill(skillId, request));
    }

    @Test
    void updateSkill_shouldThrowInvalidSkillUpdateException() {
        Skill existing = new Skill(skillId, Category.DEVELOPMENT, "React", "storage-key", false);
        UpdateSkillRequest request = new UpdateSkillRequest(Category.ARCHITECTURE, "");

        when(skillRepository.findById(skillId)).thenReturn(Optional.of(existing));

        assertThrows(InvalidSkillUpdateException.class,
                () -> skillService.updateSkill(skillId, request));
    }

    @Test
    void updateSkill_shouldReturnUpdatedResponse() {
        Skill existing = new Skill(skillId, Category.DEVELOPMENT, "React", "storage-key", false);
        UpdateSkillRequest request = new UpdateSkillRequest(Category.DEVELOPMENT, "ReactJS");

        Skill updated = new Skill(skillId, Category.DEVELOPMENT, "ReactJS", "storage-key", false);

        when(skillRepository.findById(skillId)).thenReturn(Optional.of(existing));
        when(skillRepository.save(any(Skill.class))).thenReturn(updated);
        when(storageService.generatePresignedUrl(existing.getStorageKey())).thenReturn("url");
        when(skillMapper.skillToSkillResponse(updated, "url")).thenReturn(new SkillResponse(skillId, Category.DEVELOPMENT, "ReactJS", "url"));

        SkillResponse response = skillService.updateSkill(skillId, request);

        assertEquals("ReactJS", response.name());
        assertEquals(skillId, response.skillId());
        assertEquals(Category.DEVELOPMENT, response.category());
        assertEquals("url", response.url());

        verify(skillRepository).findById(skillId);
        verify(skillRepository).save(any(Skill.class));
        verify(storageService).generatePresignedUrl("storage-key");
        verify(skillMapper).skillToSkillResponse(updated, "url");
    }

    @Test
    void uploadSkillFile_shouldThrowSkillNotFoundException() throws IOException {
        MultipartFile file = mock(MultipartFile.class);

        when(skillRepository.findById(skillId)).thenReturn(Optional.empty());

        assertThrows(SkillNotFoundException.class,
                () -> skillService.uploadSkillFile(skillId, file));
    }

    @Test
    void uploadSkillFile_shouldThrowFileProcessingException() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getBytes()).thenThrow(new IOException("IO error"));

        Skill existing = new Skill(skillId, Category.DEVELOPMENT, "React", "old-key", false);
        when(skillRepository.findById(skillId)).thenReturn(Optional.of(existing));

        assertThrows(FileProcessingException.class,
                () -> skillService.uploadSkillFile(skillId, file));
    }

    @Test
    void uploadSkillFile_shouldDeleteOldFileAndUploadNew() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getBytes()).thenReturn("dummy".getBytes());

        Skill existing = new Skill(skillId, Category.DEVELOPMENT, "React", "old-key", false);
        StorageResult storageResult = new StorageResult("new-key", "new-url");
        Skill updated = new Skill(skillId, Category.DEVELOPMENT, "React", "new-key", false);

        when(skillRepository.findById(skillId)).thenReturn(Optional.of(existing));
        when(storageService.upload(any())).thenReturn(storageResult);
        when(skillRepository.save(existing)).thenReturn(updated);
        when(storageService.generatePresignedUrl("new-key")).thenReturn("new-url");
        when(skillMapper.skillToSkillResponse(updated, "new-url"))
                .thenReturn(new SkillResponse(skillId, Category.DEVELOPMENT, "React", "new-url"));

        SkillResponse response = skillService.uploadSkillFile(skillId, file);

        assertEquals(skillId, response.skillId());
        assertEquals("React", response.name());
        assertEquals(Category.DEVELOPMENT, response.category());
        assertEquals("new-url", response.url());

        verify(storageService).delete("old-key");
        verify(storageService).upload(any());
        verify(skillRepository).save(existing);
        verify(skillMapper).skillToSkillResponse(updated, "new-url");
    }

    @Test
    void getSkillsByCategory_shouldReturnMappedResponses() {
        Skill skill = new Skill(skillId, Category.DEVELOPMENT, "React", "storage-key", false);

        when(skillRepository.findByCategory(Category.DEVELOPMENT)).thenReturn(List.of(skill));
        when(storageService.generatePresignedUrl("storage-key")).thenReturn("url");
        when(skillMapper.skillToSkillResponse(skill, "url"))
                .thenReturn(new SkillResponse(skillId, Category.DEVELOPMENT, "React", "url"));

        List<SkillResponse> responses = skillService.getSkillsByCategory(Category.DEVELOPMENT);

        assertEquals(1, responses.size());
        assertEquals("React", responses.get(0).name());
        assertEquals("url", responses.get(0).url());

        verify(skillRepository).findByCategory(Category.DEVELOPMENT);
        verify(storageService).generatePresignedUrl("storage-key");
        verify(skillMapper).skillToSkillResponse(skill, "url");
    }

    @Test
    void getSkillsByCategory_shouldFilterOutDeletedSkills() {
        Skill deleted = new Skill(skillId, Category.DEVELOPMENT, "React", "storage-key", true);

        when(skillRepository.findByCategory(Category.DEVELOPMENT)).thenReturn(List.of(deleted));

        List<SkillResponse> responses = skillService.getSkillsByCategory(Category.DEVELOPMENT);

        assertTrue(responses.isEmpty());
        verify(skillRepository).findByCategory(Category.DEVELOPMENT);
        verify(storageService, never()).generatePresignedUrl(anyString());
        verify(skillMapper, never()).skillToSkillResponse(any(), any());
    }

    @Test
    void getAllSkills_shouldReturnMappedResponses() {
        Skill skill1 = new Skill(skillId, Category.DEVELOPMENT, "React", "storage-key1", false);
        Skill skill2 = new Skill(UUID.randomUUID(), Category.ARCHITECTURE, "Design", "storage-key2", false);

        when(skillRepository.findAll()).thenReturn(List.of(skill1, skill2));
        when(storageService.generatePresignedUrl("storage-key1")).thenReturn("url1");
        when(storageService.generatePresignedUrl("storage-key2")).thenReturn("url2");
        when(skillMapper.skillToSkillResponse(skill1, "url1"))
                .thenReturn(new SkillResponse(skillId, Category.DEVELOPMENT, "React", "url1"));
        when(skillMapper.skillToSkillResponse(skill2, "url2"))
                .thenReturn(new SkillResponse(skill2.getSkillId(), Category.ARCHITECTURE, "Design", "url2"));

        List<SkillResponse> responses = skillService.getAllSkills();

        assertEquals(2, responses.size());
        assertEquals("React", responses.get(0).name());
        assertEquals("Design", responses.get(1).name());

        verify(skillRepository).findAll();
        verify(storageService).generatePresignedUrl("storage-key1");
        verify(storageService).generatePresignedUrl("storage-key2");
        verify(skillMapper).skillToSkillResponse(skill1, "url1");
        verify(skillMapper).skillToSkillResponse(skill2, "url2");
    }

    @Test
    void getAllSkills_shouldFilterOutDeletedSkills() {
        Skill deleted = new Skill(skillId, Category.DEVELOPMENT, "React", "storage-key", true);

        when(skillRepository.findAll()).thenReturn(List.of(deleted));

        List<SkillResponse> responses = skillService.getAllSkills();

        assertTrue(responses.isEmpty());
        verify(skillRepository).findAll();
        verify(storageService, never()).generatePresignedUrl(anyString());
        verify(skillMapper, never()).skillToSkillResponse(any(), any());
    }

    @Test
    void deleteSkill_shouldThrowSkillNotFoundException() {
        when(skillRepository.findById(skillId)).thenReturn(Optional.empty());

        assertThrows(SkillNotFoundException.class,
                () -> skillService.deleteSkill(skillId));
    }

    @Test
    void deleteSkill_shouldDeleteSkill() {
        Skill skill = new Skill(skillId, Category.DEVELOPMENT, "React", "storage-key", false);

        when(skillRepository.findById(skillId)).thenReturn(Optional.of(skill));

        skillService.deleteSkill(skillId);

        ArgumentCaptor<Skill> captor = ArgumentCaptor.forClass(Skill.class);
        verify(skillRepository).save(captor.capture());

        Skill savedSkill = captor.getValue();
        assertTrue(savedSkill.getIsDeleted());
    }

    @Test
    void deleteSkillHard_shouldThrowSkillNotFoundException() {
        when(skillRepository.findById(skillId)).thenReturn(Optional.empty());

        assertThrows(SkillNotFoundException.class,
                () -> skillService.deleteSkillHard(skillId));
    }

    @Test
    void deleteSkillHard_shouldDeleteSkillAndFile() {
        Skill skill = new Skill(skillId, Category.DEVELOPMENT, "React", "storage-key", false);

        when(skillRepository.findById(skillId)).thenReturn(Optional.of(skill));

        skillService.deleteSkillHard(skillId);

        verify(skillRepository).delete(skill);
        verify(storageService).delete("storage-key");
    }
}
