package com.portfolio.portfolio_service.skill.storage;

import com.portfolio.portfolio_service.skill.storage.dtos.StorageResult;

public interface StorageService {
    StorageResult upload(byte[] data);
    String generatePresignedUrl(String key);
    void delete(String key);
}
